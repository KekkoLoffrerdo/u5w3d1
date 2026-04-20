package com.example.u5w2d5esame.services;

import com.example.u5w2d5esame.entities.Dipendente;
import com.example.u5w2d5esame.exceptions.NotFoundException;
import com.example.u5w2d5esame.exceptions.UnauthorizedException;
import com.example.u5w2d5esame.payloads.LoginDTO;
import com.example.u5w2d5esame.security.TokenTools;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DipendentiService dipendentiService;
    private final TokenTools tokenTools;

    public AuthService(DipendentiService dipendentiService, TokenTools tokenTools) {
        this.dipendentiService = dipendentiService;
        this.tokenTools = tokenTools;
    }

    public String login(LoginDTO body) {
        try {
            Dipendente found = this.dipendentiService.findByEmail(body.email());

            if (found.getPassword().equals(body.password())) {
                return this.tokenTools.generateToken(found);
            } else {
                throw new UnauthorizedException("Credenziali errate");
            }

        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}