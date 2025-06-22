package com.nest.authservice.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    @Builder.Default
    private Instant timestamp = Instant.now();
    private int status;
    private String message;
    private String error;
    private String path;
}
