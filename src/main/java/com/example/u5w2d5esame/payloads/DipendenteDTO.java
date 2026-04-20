package com.example.u5w2d5esame.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DipendenteDTO(
        @NotBlank(message = "Lo username è obbligatorio")
        @Size(min = 2, max = 10, message = "Lo username deve essere compreso tra 2 e 10 caratteri")
        String username,

        @NotBlank(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 10, message = "Il nome deve essere compreso tra 2 e 10 caratteri")
        String nome,

        @NotBlank(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 10, message = "Il cognome deve essere compreso tra 2 e 10 caratteri")
        String cognome,

        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è corretta")
        String email,

        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri")
        String password
) {
}
