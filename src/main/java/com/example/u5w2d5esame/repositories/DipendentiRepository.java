package com.example.u5w2d5esame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.u5w2d5esame.entities.Dipendente;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DipendentiRepository extends JpaRepository<Dipendente, UUID> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<Dipendente> findByEmail(String email);
}