package com.example.u5w2d5esame.controllers;

import com.example.u5w2d5esame.entities.Dipendente;
import com.example.u5w2d5esame.exceptions.ValidationException;
import com.example.u5w2d5esame.payloads.DipendenteDTO;
import com.example.u5w2d5esame.payloads.NewDipendenteRespDTO;
import com.example.u5w2d5esame.services.DipendentiService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/dipendenti")
public class DipendentiController {

    private final DipendentiService dipendentiService;

    public DipendentiController(DipendentiService dipendentiService) {
        this.dipendentiService = dipendentiService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewDipendenteRespDTO saveDipendente(@RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        Dipendente newDipendente = this.dipendentiService.save(body);
        return new NewDipendenteRespDTO(newDipendente.getId());
    }

    @GetMapping
    public Page<Dipendente> getDipendenti(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sortBy) {
        return this.dipendentiService.findAll(page, size, sortBy);
    }

    @GetMapping("/{id}")
    public Dipendente getDipendenteById(@PathVariable("id") UUID id) {
        return this.dipendentiService.findById(id);
    }

    @PutMapping("/{id}")
    public Dipendente findByIdAndUpdate(@PathVariable("id") UUID id, @RequestBody @Validated DipendenteDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        return this.dipendentiService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable("id") UUID id) {
        this.dipendentiService.findByIdAndDelete(id);
    }

    @PatchMapping("/{id}/avatar")
    public void uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable("id") UUID id) {
        this.dipendentiService.avatarUpload(file, id);
    }
}
