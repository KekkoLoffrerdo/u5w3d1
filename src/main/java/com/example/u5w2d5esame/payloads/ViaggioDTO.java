package com.example.u5w2d5esame.payloads;

import com.example.u5w2d5esame.enums.StatoViaggio;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ViaggioDTO(
        @NotBlank(message = "La destinazione è obbligatoria")
        String destinazione,

        @NotNull(message = "La data è obbligatoria")
        @FutureOrPresent(message = "La data del viaggio non può essere nel passato")
        LocalDate data,

        @NotNull(message = "Lo stato è obbligatorio")
        StatoViaggio stato
) {
}
