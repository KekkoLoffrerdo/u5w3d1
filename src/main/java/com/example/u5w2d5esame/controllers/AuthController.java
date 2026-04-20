package com.example.u5w2d5esame.controllers;

import com.example.u5w2d5esame.payloads.LoginDTO;
import com.example.u5w2d5esame.payloads.LoginRespDTO;
import com.example.u5w2d5esame.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        return new LoginRespDTO(this.authService.login(body));
    }
}
