package com.nest.authservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nest.authservice.exception.ErrorResponse;
import com.nest.authservice.service.JwtService;
import com.nest.authservice.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var roles = jwtService.extractRoles(jwt);
                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userEmail, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.debug("Authenticated user: {} with roles: {}", userEmail, authorities);
            }
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException ex){
            log.warn("JWT validation failed: {},{}", ex.getMessage(),ex);

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now()) // add timestamp
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .error("Unauthorized")
                    .message(ex.getMessage())
                    .path(request.getRequestURI())
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.writeValue(response.getWriter(), errorResponse);
        }
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/auth/signup") || path.equals("/auth/login");
    }

}
