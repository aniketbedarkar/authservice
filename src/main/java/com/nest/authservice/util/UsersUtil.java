package com.nest.authservice.util;

import com.nest.authservice.model.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class UsersUtil {
    public static Optional<Users> getUserFromAuthentication(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth.getPrincipal() instanceof Users users){
            return Optional.of(users);
        }
        else {
            return Optional.empty();
        }
    }
}
