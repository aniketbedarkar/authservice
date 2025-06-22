package com.nest.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key signinKey;

    @PostConstruct
    public void init(){
        signinKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetails userDetails) {
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(signinKey, SignatureAlgorithm.HS256)
                .compact();
    }
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String jwt, UserDetails user) {
        final String username = extractUsername(jwt);
        return (username.equals(user.getUsername()) && !isTokenExpired(jwt));
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signinKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
