package com.example.turnos_medicos.controller;

import com.example.turnos_medicos.entity.Turno;
import com.example.turnos_medicos.service.TurnoService;
import com.example.turnos_medicos.dto.TurnoDTO;
import com.example.turnos_medicos.dto.EstadoTurnoUpdateDTO;
import com.example.turnos_medicos.repository.PersonaRepository;
import com.example.turnos_medicos.entity.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TurnoController {
    private final TurnoService turnoService;
    private final PersonaRepository personaRepository;

    @Autowired
    public TurnoController(TurnoService turnoService, PersonaRepository personaRepository) {
        this.turnoService = turnoService;
        this.personaRepository = personaRepository;
    }

    // GET /api/public/turnos
    @GetMapping("/public/turnos")
    public ResponseEntity<List<TurnoDTO>> getTurnosPublicos() {
        List<Turno> turnos = turnoService.findAll().stream()
            .filter(t -> Turno.EstadoTurno.DISPONIBLE.equals(t.getEstado()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(turnos.stream().map(this::mapTurnoToDTO).collect(Collectors.toList()));
    }

    // POST /api/private/turnos — Solo el admin crea turnos
    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("/private/turnos")
    public ResponseEntity<TurnoDTO> createTurno(@RequestBody Turno turno) {
        turno.setEstado(Turno.EstadoTurno.DISPONIBLE);
        Turno created = turnoService.save(turno);
        return ResponseEntity.ok(mapTurnoToDTO(created));
    }

    // PUT /api/private/turnos/{id}/reservar
    @PreAuthorize("hasRole('ROLE_usuario')")
    @PutMapping("/private/turnos/{id}/reservar")
    public ResponseEntity<TurnoDTO> reservarTurno(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Optional<Turno> turnoOpt = turnoService.findById(id);
        if (turnoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Turno turno = turnoOpt.get();
        if (!turno.getEstado().equals(Turno.EstadoTurno.DISPONIBLE)) {
            return ResponseEntity.badRequest().build();
        }
        turno.setEstado(Turno.EstadoTurno.RESERVADO);
        turno.setUserId(jwt.getSubject());
        Turno updated = turnoService.save(turno);
        return ResponseEntity.ok(mapTurnoToDTO(updated));
    }

    // GET /api/private/turnos — Recepcionista o admin ven todos los turnos
    @PreAuthorize("hasAnyRole('ROLE_recepcionista','ROLE_admin')")
    @GetMapping("/private/turnos")
    public ResponseEntity<List<TurnoDTO>> getAllTurnos() {
        List<Turno> turnos = turnoService.findAll();
        List<TurnoDTO> dtos = turnos.stream().map(this::mapTurnoToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /api/private/turnos/mis-citas
    @PreAuthorize("hasRole('ROLE_usuario')")
    @GetMapping("/private/turnos/mis-citas")
    public ResponseEntity<List<TurnoDTO>> getMyTurnos(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        List<Turno> turnos = turnoService.findByUserIdAndEstado(userId, Turno.EstadoTurno.RESERVADO);
        List<TurnoDTO> dtos = turnos.stream().map(this::mapTurnoToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // PUT /api/private/turnos/{id}/estado
    @PreAuthorize("hasAnyRole('ROLE_recepcionista','ROLE_admin')")
    @PutMapping("/private/turnos/{id}/estado")
    public ResponseEntity<TurnoDTO> updateTurnoStatus(@PathVariable Long id, @RequestBody EstadoTurnoUpdateDTO estadoUpdate) {
        Optional<Turno> turnoOpt = turnoService.findById(id);
        if (turnoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Turno turno = turnoOpt.get();
        turno.setEstado(estadoUpdate.getEstado());
        
        // Si el turno vuelve a estar disponible o se cancela, limpia el userId
        if (estadoUpdate.getEstado() == Turno.EstadoTurno.DISPONIBLE || 
            estadoUpdate.getEstado() == Turno.EstadoTurno.CANCELADO) {
            turno.setUserId(null);
        }

        Turno updated = turnoService.save(turno);
        return ResponseEntity.ok(mapTurnoToDTO(updated));
    }

    private TurnoDTO mapTurnoToDTO(Turno turno) {
        String customerEmail = null;
        // muestra el email si el turno esta reservado
        if (turno.getUserId() != null && turno.getEstado() == Turno.EstadoTurno.RESERVADO) {
            customerEmail = personaRepository.findByAuth0Id(turno.getUserId())
                .map(Persona::getEmail).orElse(null);
        }
        return new TurnoDTO(
            turno.getId(),
            turno.getFecha(),
            turno.getHorario(),
            turno.getEstado(),
            turno.getUbicacion(),
            customerEmail,
            turno.getCreatedAt(),
            turno.getUpdatedAt()
        );
    }
}