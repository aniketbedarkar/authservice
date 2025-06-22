package com.nest.authservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestUpdateUser {

    private String firstName;

    private String lastName;

    private String email;

    private Set<Long> roles;
}
