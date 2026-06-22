package com.example.turnos_medicos.controller;

import com.example.turnos_medicos.entity.Turno;
import com.example.turnos_medicos.service.TurnoService;
import com.example.turnos_medicos.dto.TurnoDTO;
import com.example.turnos_medicos.repository.PersonaRepository;
import com.example.turnos_medicos.entity.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/private/turnos")
public class TurnoController {
    private final TurnoService turnoService;
    private final PersonaRepository personaRepository;

    @Autowired
    public TurnoController(TurnoService turnoService, PersonaRepository personaRepository) {
        this.turnoService = turnoService;
        this.personaRepository = personaRepository;
    }

    // POST /private/turnos (USUARIO)
    @PreAuthorize("hasRole('USUARIO')")
    @PostMapping
    public ResponseEntity<TurnoDTO> createTurno(@RequestBody Turno turno, @AuthenticationPrincipal Jwt jwt) {
        turno.setUserId(jwt.getSubject());
        turno.setDisponible("DISPONIBLE"); 
        turno.setCreatedAt(LocalDateTime.now());
        turno.setUpdatedAt(LocalDateTime.now());
        Turno created = turnoService.save(turno);
        TurnoDTO dto = mapTurnoToDTO(created);
        return ResponseEntity.ok(dto);
    }

    // GET /private/turnos (RECEPCIONISTA o ADMIN)
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','ADMIN')")
    @GetMapping
    public ResponseEntity<List<TurnoDTO>> getAllTurnos() {
        List<Turno> turnos = turnoService.findAll();
        List<TurnoDTO> dtos = turnos.stream().map(this::mapTurnoToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /private/turnos/my (USUARIO)
    @PreAuthorize("hasRole('USUARIO')")
    @GetMapping("/my")
    public ResponseEntity<List<TurnoDTO>> getMyTurnos(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        List<Turno> turnos = turnoService.findByUserId(userId);
        List<TurnoDTO> dtos = turnos.stream().map(this::mapTurnoToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // PUT /private/turnos/{id}/status (RECEPCIONISTA o ADMIN)
    @PreAuthorize("hasAnyRole('RECEPCIONISTA','ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Turno> updateTurnoStatus(@PathVariable Long id, @RequestBody DisponibleUpdateRequest disponibleUpdate) {
        Optional<Turno> turnoOpt = turnoService.findById(id);
        if (turnoOpt.isPresent()) {
            Turno turno = turnoOpt.get();
            turno.setDisponible(disponibleUpdate.getDisponible()); 
            turno.setUpdatedAt(LocalDateTime.now());
            Turno updated = turnoService.save(turno);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DTO interno cambiado para recibir 'disponible' desde el frontend
    public static class DisponibleUpdateRequest {
        private String disponible;
        public String getDisponible() { return disponible; }
        public void setDisponible(String disponible) { this.disponible = disponible; }
    }

    // --- Métodos de mapeo corregidos con la propiedad .getDisponible() ---
    private TurnoDTO mapTurnoToDTO(Turno turno) {
        String customerEmail = personaRepository.findByAuth0Id(turno.getUserId())
            .map(Persona::getEmail).orElse(null);
        
        return new TurnoDTO(
            turno.getId(),
            turno.getFecha(),
            turno.getHorario(),
            turno.getDisponible(),
            turno.getUbicacion(),
            customerEmail,
            turno.getCreatedAt(),
            turno.getUpdatedAt()
        );
    }
}