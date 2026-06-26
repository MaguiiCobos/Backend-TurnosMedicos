package com.example.turnos_medicos.controller;

import com.example.turnos_medicos.dto.CreateRecepcionistaRequestDTO;
import com.example.turnos_medicos.dto.UserRecepcionistaDTO;
import com.example.turnos_medicos.entity.Persona;
import com.example.turnos_medicos.repository.PersonaRepository;
import com.example.turnos_medicos.service.Auth0Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/private/users")
public class RecepcionistaController {
    private final PersonaRepository personaRepository;
    private final Auth0Service auth0Service;

    @Autowired
    public RecepcionistaController(PersonaRepository personaRepository, Auth0Service auth0Service) {
        this.personaRepository = personaRepository;
        this.auth0Service = auth0Service;
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResponseEntity<List<UserRecepcionistaDTO>> getRecepcionistas(@RequestParam(required = false) String role) {
        List<Persona> personas;
        if (role != null && role.equalsIgnoreCase("recepcionista")) {
            personas = personaRepository.findAll().stream()
                .filter(p -> p.getRol() != null && p.getRol().equalsIgnoreCase("recepcionista"))
                .collect(Collectors.toList());
        } else {
            personas = personaRepository.findAll();
        }
        List<UserRecepcionistaDTO> dtos = personas.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<?> createRecepcionista(@RequestBody CreateRecepcionistaRequestDTO request) {
        // Valida que el email no exista en la base local
        if (personaRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }
        // Crea usuario en Auth0
        Map<String, Object> auth0User = auth0Service.createAuth0User(request.getEmail(), request.getPassword());
        String userId = (String) auth0User.get("user_id");
        if (userId == null) {
            return ResponseEntity.status(500).body("Error creando usuario en Auth0");
        }
        // Asigna rol de recepcionista en Auth0
        auth0Service.assignRecepcionistaRoleToUser(userId);
        Persona persona = new Persona();
        persona.setEmail(request.getEmail());
        persona.setNombre(request.getNombre());
        persona.setRol("recepcionista");
        persona.setAuth0Id(userId);
        Persona saved = personaRepository.save(persona);
        UserRecepcionistaDTO dto = mapToDTO(saved);
        return ResponseEntity.ok(dto);
    }

    private UserRecepcionistaDTO mapToDTO(Persona persona) {
        return new UserRecepcionistaDTO(
            persona.getId(),
            persona.getEmail(),
            persona.getNombre(),
            List.of(persona.getRol())
        );
    }
} 
