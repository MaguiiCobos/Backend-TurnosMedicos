package com.example.turnos_medicos.repository;

import com.example.turnos_medicos.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByAuth0Id(String auth0Id);
    Optional<Persona> findByEmail(String email);
} 
