package com.example.turnos_medicos.service;

import com.example.turnos_medicos.entity.Turno;
import com.example.turnos_medicos.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {
    
    private final TurnoRepository turnoRepository;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    public Turno save(Turno turno) {
 
        return turnoRepository.save(turno);
    }

    public List<Turno> findAll() {
        return turnoRepository.findAll();
    }

    public Optional<Turno> findById(Long id) {
        return turnoRepository.findById(id);
    }

    public List<Turno> findByUserId(String userId) {
        return turnoRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        turnoRepository.deleteById(id);
    }
}