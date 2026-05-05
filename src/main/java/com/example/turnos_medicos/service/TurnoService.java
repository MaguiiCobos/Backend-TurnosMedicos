package com.example.turnos_medicos.service;

import com.example.turnos_medicos.model.Turno;
import com.example.turnos_medicos.repository.TurnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnoService {

    private final TurnoRepository repo;

    public TurnoService(TurnoRepository repo) {
        this.repo = repo;
    }

    public List<Turno> obtenerTurnos() {
        return repo.findAll();
    }

    public Turno reservarTurno(Long id, String email) {
        Turno turno = repo.findById(id).orElseThrow();

        if (!turno.isDisponible()) {
            throw new RuntimeException("Turno no disponible");
        }

        turno.setDisponible(false);
        turno.setPacienteEmail(email);

        return repo.save(turno);
    }
}
