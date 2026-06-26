package com.example.turnos_medicos.controller;

import org.springframework.security.oauth2.jwt.Jwt;
import com.example.turnos_medicos.entity.Persona;
import com.example.turnos_medicos.service.PersonaSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class UsuarioController {
    @Autowired
    private PersonaSyncService personaSyncService;

    @GetMapping("/me")
    public ResponseEntity<Persona> getMe(@AuthenticationPrincipal Jwt jwt) {
        Persona persona = personaSyncService.syncPersonaFromJwt(jwt);
        return ResponseEntity.ok(persona);
    }
} 
