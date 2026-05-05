package com.example.turnos_medicos.repository;

import com.example.turnos_medicos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
}
