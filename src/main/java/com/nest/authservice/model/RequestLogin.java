package com.nest.authservice.model;

import lombok.Data;

@Data
public class RequestLogin {
    private String email;
    private String password;
}
