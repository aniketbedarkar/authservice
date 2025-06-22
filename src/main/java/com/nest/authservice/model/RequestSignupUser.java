package com.nest.authservice.model;

import com.nest.authservice.annotations.ValidDecodedPassword;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSignupUser {
    @NotBlank(message = "User should have a firstname")
    private String firstName;

    @NotBlank(message = "User should have a lastname")
    private String lastName;

    @Email(message = "Email should be valid. ex: example@domain.com")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email should match pattern: example@domain.com"
    )
    private String email;

    @ValidDecodedPassword
    private String password;

    private LocalDateTime updatedAt;

    private Set<Long> roles;
}
