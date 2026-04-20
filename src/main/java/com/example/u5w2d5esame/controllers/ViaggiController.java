package com.example.u5w2d5esame.controllers;

import com.example.u5w2d5esame.entities.Viaggio;
import com.example.u5w2d5esame.exceptions.ValidationException;
import com.example.u5w2d5esame.payloads.NewViaggioRespDTO;
import com.example.u5w2d5esame.payloads.ViaggioDTO;
import com.example.u5w2d5esame.services.ViaggiService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/viaggi")
public class ViaggiController {

    private final ViaggiService viaggiService;

    public ViaggiController(ViaggiService viaggiService) {
        this.viaggiService = viaggiService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewViaggioRespDTO saveViaggio(@RequestBody @Validated ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        Viaggio newViaggio = this.viaggiService.save(body);
        return new NewViaggioRespDTO(newViaggio.getId());
    }
    @GetMapping
    public Page<Viaggio> getViaggi(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data") String sortBy) {
        return this.viaggiService.findAll(page, size, sortBy);
    }
    @GetMapping("/{id}")
    public Viaggio getViaggioById(@PathVariable("id") UUID id) {
        return this.viaggiService.findById(id);
    }
    @PutMapping("/{id}")
    public Viaggio findByIdAndUpdate(@PathVariable("id") UUID id, @RequestBody @Validated ViaggioDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            throw new ValidationException(errors);
        }

        return this.viaggiService.findByIdAndUpdate(id, body);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable("id") UUID id) {
        this.viaggiService.findByIdAndDelete(id);
    }
}
