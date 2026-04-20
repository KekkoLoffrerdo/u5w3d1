package com.example.u5w2d5esame.services;

import com.example.u5w2d5esame.entities.Viaggio;
import com.example.u5w2d5esame.exceptions.NotFoundException;
import com.example.u5w2d5esame.payloads.ViaggioDTO;
import com.example.u5w2d5esame.repositories.ViaggiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ViaggiService {

    private final ViaggiRepository viaggiRepository;

    public ViaggiService(ViaggiRepository viaggiRepository) {
        this.viaggiRepository = viaggiRepository;
    }
    public Viaggio save(ViaggioDTO body) {
        Viaggio newViaggio = new Viaggio(
                body.destinazione(),
                body.data(),
                body.stato()
        );

        Viaggio savedViaggio = this.viaggiRepository.save(newViaggio);
        log.info("Il viaggio con id {} è stato salvato correttamente", savedViaggio.getId());
        return savedViaggio;
    }
    public Page<Viaggio> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 1) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggiRepository.findAll(pageable);
    }
    public Viaggio findById(UUID id) {
        return this.viaggiRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
    public Viaggio findByIdAndUpdate(UUID id, ViaggioDTO body) {
        Viaggio found = this.findById(id);

        found.setDestinazione(body.destinazione());
        found.setData(body.data());
        found.setStato(body.stato());

        Viaggio updatedViaggio = this.viaggiRepository.save(found);
        log.info("Il viaggio con id {} è stato aggiornato correttamente", updatedViaggio.getId());
        return updatedViaggio;
    }
    public void findByIdAndDelete(UUID id) {
        Viaggio found = this.findById(id);
        this.viaggiRepository.delete(found);
    }
}
