package com.example.u5w2d5esame.services;

import com.example.u5w2d5esame.entities.Dipendente;
import com.example.u5w2d5esame.entities.Prenotazione;
import com.example.u5w2d5esame.entities.Viaggio;
import com.example.u5w2d5esame.exceptions.BadRequestException;
import com.example.u5w2d5esame.exceptions.NotFoundException;
import com.example.u5w2d5esame.payloads.PrenotazioneDTO;
import com.example.u5w2d5esame.repositories.PrenotazioniRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PrenotazioniService {

    private final PrenotazioniRepository prenotazioniRepository;
    private final DipendentiService dipendentiService;
    private final ViaggiService viaggiService;

    public PrenotazioniService(PrenotazioniRepository prenotazioniRepository, DipendentiService dipendentiService, ViaggiService viaggiService) {
        this.prenotazioniRepository = prenotazioniRepository;
        this.dipendentiService = dipendentiService;
        this.viaggiService = viaggiService;
    }

    public Prenotazione save(PrenotazioneDTO body) {
        Dipendente dipendente = this.dipendentiService.findById(body.dipendenteId());
        Viaggio viaggio = this.viaggiService.findById(body.viaggioId());

        if (this.prenotazioniRepository.existsByDipendente_IdAndViaggio_Data(dipendente.getId(), viaggio.getData())) {
            throw new BadRequestException("Il dipendente ha già una prenotazione per questa data");
        }

        Prenotazione newPrenotazione = new Prenotazione(
                body.dataRichiesta(),
                body.note(),
                body.preferenze(),
                dipendente,
                viaggio
        );

        Prenotazione savedPrenotazione = this.prenotazioniRepository.save(newPrenotazione);
        log.info("La prenotazione con id {} è stata salvata correttamente", savedPrenotazione.getId());
        return savedPrenotazione;
    }

    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 1) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioniRepository.findAll(pageable);
    }

    public Prenotazione findById(UUID id) {
        return this.prenotazioniRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Prenotazione findByIdAndUpdate(UUID id, PrenotazioneDTO body) {
        Prenotazione found = this.findById(id);
        Dipendente dipendente = this.dipendentiService.findById(body.dipendenteId());
        Viaggio viaggio = this.viaggiService.findById(body.viaggioId());

        if ((!found.getDipendente().getId().equals(dipendente.getId()) || !found.getViaggio().getId().equals(viaggio.getId()))
                && this.prenotazioniRepository.existsByDipendente_IdAndViaggio_Data(dipendente.getId(), viaggio.getData())) {
            throw new BadRequestException("Il dipendente ha già una prenotazione per questa data");
        }

        found.setDataRichiesta(body.dataRichiesta());
        found.setNote(body.note());
        found.setPreferenze(body.preferenze());
        found.setDipendente(dipendente);
        found.setViaggio(viaggio);

        Prenotazione updatedPrenotazione = this.prenotazioniRepository.save(found);
        log.info("La prenotazione con id {} è stata aggiornata correttamente", updatedPrenotazione.getId());
        return updatedPrenotazione;
    }

    public void findByIdAndDelete(UUID id) {
        Prenotazione found = this.findById(id);
        this.prenotazioniRepository.delete(found);
    }
}
