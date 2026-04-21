package com.example.u5w2d5esame.services;

import com.example.u5w2d5esame.entities.Dipendente;
import com.example.u5w2d5esame.exceptions.NotFoundException;
import com.example.u5w2d5esame.exceptions.UnauthorizedException;
import com.example.u5w2d5esame.payloads.LoginDTO;
import com.example.u5w2d5esame.security.TokenTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DipendentiService dipendentiService;
    private final TokenTools tokenTools;
    private final PasswordEncoder passwordEncoder;

    public AuthService(DipendentiService dipendentiService,
                       TokenTools tokenTools,
                       PasswordEncoder passwordEncoder) {
        this.dipendentiService = dipendentiService;
        this.tokenTools = tokenTools;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginDTO body) {
        System.out.println(">>> ENTRO NEL SERVICE");

        try {
            Dipendente found = this.dipendentiService.findByEmail(body.email());
            System.out.println("Utente trovato: " + found.getEmail());

            if (passwordEncoder.matches(body.password(), found.getPassword())) {
                System.out.println("Password OK");
                return this.tokenTools.generateToken(found);
            } else {
                System.out.println("Password SBAGLIATA");
                throw new UnauthorizedException("Credenziali errate");
            }

        } catch (NotFoundException ex) {
            System.out.println("Utente NON trovato");
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}