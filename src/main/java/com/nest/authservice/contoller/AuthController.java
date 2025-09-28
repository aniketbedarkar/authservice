package com.nest.authservice.contoller;

import com.nest.authservice.exception.ErrorResponse;
import com.nest.authservice.model.*;
import com.nest.authservice.service.AuthService;
import com.nest.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/allUsers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok().body(authService.getAllUsers());
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String header){
        ResponseUserDTO responseUserDTO = userService.getUser();
        return ResponseEntity.ok().body(responseUserDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid RequestSignupUser requestSignupUser){
        Optional<ResponseUserDTO> optionalResponseUserDTO = userService.signUp(requestSignupUser);
        if(optionalResponseUserDTO.isPresent()){
            return ResponseEntity.ok().body(optionalResponseUserDTO.get());
        }else {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .error("Internal Server Error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RequestLogin requestLogin){
        AuthResponse authResponse = authService.login(requestLogin);
        return ResponseEntity.ok().body(authResponse);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody @Valid RequestUpdateUser requestUpdateUser){
        Optional<ResponseUserDTO> optionalResponseUserDTO = userService.updateUser(requestUpdateUser);
        if(optionalResponseUserDTO.isPresent()){
            return ResponseEntity.ok().body(optionalResponseUserDTO.get());
        }else {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .error("Internal Server Error")
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id){
        userService.deleteById(id);
        return ResponseEntity.ok().body("User deleted successfully");
    }

}
