package com.example.u5w2d5esame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.u5w2d5esame.entities.Prenotazione;
import java.util.UUID;
import java.time.LocalDate;

@Repository
public interface PrenotazioniRepository extends JpaRepository<Prenotazione, UUID> {
    boolean existsByDipendente_IdAndViaggio_Data(UUID dipendenteId, LocalDate data);
}
