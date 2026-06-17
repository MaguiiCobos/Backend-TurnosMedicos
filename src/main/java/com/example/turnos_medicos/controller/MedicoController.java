package com.example.turnos_medicos.controller;

import com.example.turnos_medicos.entity.Persona;
import com.example.turnos_medicos.repository.PersonaRepository;
import com.example.turnos_medicos.service.Auth0Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/usuarios")
public class MedicoController {
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private Auth0Service auth0Service;

    // Elimino el endpoint de cambio de rol porque la sincronización con Auth0 no se usará

    @GetMapping("")
    public ResponseEntity<Iterable<Persona>> listarUsuarios() {
        return ResponseEntity.ok(personaRepository.findAll());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Persona> buscarUsuario(@RequestParam(required = false) String email, @RequestParam(required = false) String nombre) {
        if (email != null) {
            return personaRepository.findByEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else if (nombre != null) {
            // Buscar por nombre exacto (puedes mejorar esto para búsqueda parcial)
            return personaRepository.findAll().stream()
                    .filter(p -> nombre.equalsIgnoreCase(p.getNombre()))
                    .findFirst()
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> verUsuarioPorId(@PathVariable Long id) {
        return personaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

} 
