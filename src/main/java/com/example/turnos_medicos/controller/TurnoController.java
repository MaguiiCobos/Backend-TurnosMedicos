package com.example.turnos_medicos.controller;

import com.example.turnos_medicos.entity.Turno;
import com.example.turnos_medicos.service.TurnoService;
import com.example.turnos_medicos.service.TurnoService;
import com.example.turnos_medicos.dto.TurnoDTO;
import com.example.turnos_medicos.dto.TurnoItemDTO;
import com.example.turnos_medicos.dto.TurnoDTO;
import com.example.turnos_medicos.dto.TurnoItemDTO;
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

    // POST /private/turnos (CLIENT)
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<TurnoDTO> createTurno(@RequestBody Turno turno, @AuthenticationPrincipal Jwt jwt) {
        turno.setUserId(jwt.getSubject());
        turno.setStatus("PENDIENTE");
        turno.setCreatedAt(LocalDateTime.now());
        turno.setUpdatedAt(LocalDateTime.now());
        Turno created = turnoService.save(turno);
        TurnoDTO dto = mapTurnoToDTO(created);
        return ResponseEntity.ok(dto);
    }

    // GET /private/turnos (BARISTA o ADMIN)
    @PreAuthorize("hasAnyRole('BARISTA','ADMIN')")
    @GetMapping
    public ResponseEntity<List<TurnoDTO>> getAllTurnos() {
        List<Turno> turnos = turnoService.findAll();
        List<TurnoDTO> dtos = turnos.stream().map(this::mapTurnoToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // GET /private/turnos/my (CLIENT)
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/my")
    public ResponseEntity<List<TurnoDTO>> getMyTurnos(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        List<Turno> turnos = turnoService.findByUserId(userId);
        List<TurnoDTO> dtos = turnos.stream().map(this::mapTurnoToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // PUT /private/turnos/{id}/status (BARISTA o ADMIN)
    @PreAuthorize("hasAnyRole('BARISTA','ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Turno> updateTurnoStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest statusUpdate) {
        Optional<Turno> turnoOpt = turnoService.findById(id);
        if (turnoOpt.isPresent()) {
            Turno turno = turnoOpt.get();
            turno.setStatus(statusUpdate.getStatus());
            turno.setUpdatedAt(LocalDateTime.now());
            Turno updated = turnoService.save(turno);
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DTO para recibir el status
    public static class StatusUpdateRequest {
        private String status;
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // --- Métodos de mapeo ---
    private TurnoDTO mapTurnoToDTO(Turno turno) {
        String customerEmail = personaRepository.findByAuth0Id(turno.getUserId())
            .map(Persona::getEmail).orElse(null);
        return new TurnoDTO(
            turno.getId(),
            turno.getStatus(),
            turno.getTotal(),
            turno.getCreatedAt(),
            turno.getUpdatedAt(),
            customerEmail,
            turno.getItems() != null ? turno.getItems().stream().map(this::mapTurnoItemToDTO).collect(Collectors.toList()) : null
        );
    }

    private TurnoItemDTO mapTurnoItemToDTO(com.example.turnos_medicos.entity.TurnoItem item) {
        String menuItemName = item.getMenuItem() != null ? item.getMenuItem().getName() : null;
        return new TurnoItemDTO(
            item.getId(),
            item.getQuantity(),
            item.getPrice(),
            menuItemName
        );
    }
} 
