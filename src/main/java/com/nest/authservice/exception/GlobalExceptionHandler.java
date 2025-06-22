package com.nest.authservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UsernameNotFoundException e, HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder().status(HttpStatus.NOT_FOUND.value()).error("User not found.").message(e.getMessage()).path(request.getRequestURI()).build();
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException e, HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder().status(HttpStatus.CONFLICT.value()).error("User already exists.").message(e.getMessage()).path(request.getRequestURI()).build();
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(RoleNotFoundException e, HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder().status(HttpStatus.NOT_FOUND.value()).error("Role not found.").message(e.getMessage()).path(request.getRequestURI()).build();
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI())
                .error("Method argument not valid.")
                .message(e.getBindingResult().getFieldErrors().stream().map(error -> "#"+error.getDefaultMessage()).toList().toString())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .error("User not found")
                .message(e.getMessage())
                .path(request.getRequestURI())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NoChangesDetectedException.class)
    public ResponseEntity<ErrorResponse> handleNoChangesDetectedException(NoChangesDetectedException ex,HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .path(request.getRequestURI())
                .error("No changes found")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request){
        log.error("Unhandled exception at [{}]: {}", request.getRequestURI(), e.getMessage(), e);

        return new ResponseEntity<>(ErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value()).error("Internal Server Error").message(e.getMessage()).path(request.getRequestURI()).build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
