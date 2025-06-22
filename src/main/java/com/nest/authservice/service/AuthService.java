package com.nest.authservice.service;

import com.nest.authservice.model.AuthResponse;
import com.nest.authservice.model.RequestLogin;
import com.nest.authservice.model.ResponseUserDTO;
import com.nest.authservice.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

import static com.nest.authservice.util.Base64Util.getBase64DecodedPassword;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public List<ResponseUserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    public AuthResponse login(RequestLogin requestLogin) {
        Users user = userService.getUserByEmail(requestLogin.getEmail());
        requestLogin.setPassword(getBase64DecodedPassword(requestLogin.getPassword()));
        if(passwordEncoder.matches(requestLogin.getPassword(), user.getPassword())){
            return AuthResponse.builder().token(jwtService.generateToken(user)).build();
        }else{
            throw new BadCredentialsException("Invalid Credentials");
        }
    }

    private static void setDecodedPassword(RequestLogin requestLogin) {
        byte[] decodedBytes = Base64.getDecoder().decode(requestLogin.getPassword());
       ;
    }
}
