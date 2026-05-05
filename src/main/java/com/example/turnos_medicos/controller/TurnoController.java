package com.example.turnos_medicos.controller;

import com.example.turnos_medicos.model.Turno;
import com.example.turnos_medicos.service.TurnoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {

    private final TurnoService service;

    public TurnoController(TurnoService service) {
        this.service = service;
    }

    // 🔓 público
    @GetMapping
    public List<Turno> listar() {
        return service.obtenerTurnos();
    }

    // 🔐 protegido
    @PostMapping("/reservar/{id}")
    public Turno reservar(@PathVariable Long id) {
        return service.reservarTurno(id, "usuario@test.com");
    }
}