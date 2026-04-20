package com.example.u5w2d5esame.payloads;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record PrenotazioneDTO(
        @NotNull(message = "La data della richiesta è obbligatoria")
        LocalDate dataRichiesta,

        String note,
        String preferenze,

        @NotNull(message = "L'id del dipendente è obbligatorio")
        UUID dipendenteId,

        @NotNull(message = "L'id del viaggio è obbligatorio")
        UUID viaggioId
) {
}
