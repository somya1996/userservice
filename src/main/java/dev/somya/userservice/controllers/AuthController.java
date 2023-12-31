package dev.somya.userservice.controllers;

import dev.somya.userservice.dtos.*;
import dev.somya.userservice.models.Session;
import dev.somya.userservice.models.SessionStatus;
import dev.somya.userservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) {
        return authService.login(request.getEmail() , request.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request){
        return authService.logout(request.getToken(), request.getUserId());
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) {
        return authService.signUp(request.getEmail(), request.getPassword() , request.getName() , request.getPhone());
    }

    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(ValidateTokenRequestDto request){
        SessionStatus sessionStatus = authService.validate(request.getUserId() , request.getToken());
        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }

}