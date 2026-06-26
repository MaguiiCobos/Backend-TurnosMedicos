package com.example.turnos_medicos.repository;

import com.example.turnos_medicos.entity.Turno;
import com.example.turnos_medicos.entity.Turno.EstadoTurno;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByUserId(String userId);
    List<Turno> findByUserIdAndEstado(String userId, EstadoTurno estado);
} 
