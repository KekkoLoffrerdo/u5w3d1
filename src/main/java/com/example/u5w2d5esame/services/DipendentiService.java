package com.example.u5w2d5esame.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.u5w2d5esame.entities.Dipendente;
import com.example.u5w2d5esame.exceptions.BadRequestException;
import com.example.u5w2d5esame.exceptions.NotFoundException;
import com.example.u5w2d5esame.payloads.DipendenteDTO;
import com.example.u5w2d5esame.repositories.DipendentiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class DipendentiService {

    private final DipendentiRepository dipendentiRepository;
    private final Cloudinary cloudinaryUploader;

    public DipendentiService(DipendentiRepository dipendentiRepository, Cloudinary cloudinaryUploader) {
        this.dipendentiRepository = dipendentiRepository;
        this.cloudinaryUploader = cloudinaryUploader;
    }

    public Dipendente save(DipendenteDTO body) {
        if (this.dipendentiRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'email " + body.email() + " è già in uso");
        }

        if (this.dipendentiRepository.existsByUsername(body.username())) {
            throw new BadRequestException("Lo username " + body.username() + " è già in uso");
        }

        Dipendente newDipendente = new Dipendente(
                body.username(),
                body.nome(),
                body.cognome(),
                body.email(),
                body.password()
        );

        Dipendente savedDipendente = this.dipendentiRepository.save(newDipendente);
        log.info("Il dipendente con id {} è stato salvato correttamente", savedDipendente.getId());
        return savedDipendente;
    }

    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        if (size > 100 || size < 1) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendentiRepository.findAll(pageable);
    }

    public Dipendente findById(UUID id) {
        return this.dipendentiRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Dipendente findByIdAndUpdate(UUID id, DipendenteDTO body) {
        Dipendente found = this.findById(id);

        if (!found.getEmail().equals(body.email()) &&
                this.dipendentiRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'email " + body.email() + " è già in uso");
        }

        if (!found.getUsername().equals(body.username()) &&
                this.dipendentiRepository.existsByUsername(body.username())) {
            throw new BadRequestException("Lo username " + body.username() + " è già in uso");
        }

        found.setUsername(body.username());
        found.setNome(body.nome());
        found.setCognome(body.cognome());
        found.setEmail(body.email());
        found.setPassword(body.password());

        Dipendente updatedDipendente = this.dipendentiRepository.save(found);
        log.info("Il dipendente con id {} è stato aggiornato correttamente", updatedDipendente.getId());

        return updatedDipendente;
    }

    public void findByIdAndDelete(UUID id) {
        Dipendente found = this.findById(id);
        this.dipendentiRepository.delete(found);
    }
    public Dipendente findByEmail(String email) {
        return this.dipendentiRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Dipendente non trovato"));
    }
    public void avatarUpload(MultipartFile file, UUID id) {
        Dipendente found = this.findById(id);

        try {
            Map result = cloudinaryUploader.uploader()
                    .upload(file.getBytes(), ObjectUtils.emptyMap());

            String url = (String) result.get("secure_url");

            found.setAvatarURL(url);
            this.dipendentiRepository.save(found);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}