package com.example.u5w2d5esame.controllers;

import com.example.u5w2d5esame.entities.Prenotazione;
import com.example.u5w2d5esame.exceptions.ValidationException;
import com.example.u5w2d5esame.payloads.NewPrenotazioneRespDTO;
import com.example.u5w2d5esame.payloads.PrenotazioneDTO;
import com.example.u5w2d5esame.services.PrenotazioniService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotazioniController {

    private final PrenotazioniService prenotazioniService;

    public PrenotazioniController(PrenotazioniService prenotazioniService) {
        this.prenotazioniService = prenotazioniService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewPrenotazioneRespDTO savePrenotazione(@RequestBody @Validated PrenotazioneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        Prenotazione newPrenotazione = this.prenotazioniService.save(body);
        return new NewPrenotazioneRespDTO(newPrenotazione.getId());
    }

    @GetMapping
    public Page<Prenotazione> getPrenotazioni(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dataRichiesta") String sortBy) {
        return this.prenotazioniService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id}")
    public Prenotazione getPrenotazioneById(@PathVariable("id") UUID id) {
        return this.prenotazioniService.findById(id);
    }

    @PutMapping("/{id}")
    public Prenotazione findByIdAndUpdate(@PathVariable("id") UUID id, @RequestBody @Validated PrenotazioneDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        return this.prenotazioniService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable("id") UUID id) {
        this.prenotazioniService.findByIdAndDelete(id);
    }
}
