package com.example.turnos_medicos.repository;

import com.example.turnos_medicos.entity.TurnoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoItemRepository extends JpaRepository<TurnoItem, Long> {
} 