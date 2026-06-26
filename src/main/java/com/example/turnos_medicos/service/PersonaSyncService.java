package com.example.turnos_medicos.service;

import org.springframework.security.oauth2.jwt.Jwt;
import com.example.turnos_medicos.entity.Persona;
import com.example.turnos_medicos.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PersonaSyncService {

    @Autowired
    private PersonaRepository personaRepository;

    public Persona syncPersonaFromJwt(Jwt jwt) {

        String auth0Id = jwt.getSubject();
        String email   = jwt.getClaimAsString("email");
        String nombre  = jwt.getClaimAsString("name");

        List<String> roles = jwt.getClaimAsStringList("https://turnos-medicos.com/roles");
        String rol = (roles != null && !roles.isEmpty()) ? roles.get(0) : "usuario";

        // Busca la persona en la bd por su auth0Id
        return personaRepository.findByAuth0Id(auth0Id)
            .map(persona -> {
                // Si existe revisamos si hay datos para actualizar
                boolean updated = false;
                if (email != null && !email.equals(persona.getEmail())) {
                    persona.setEmail(email);
                    updated = true;
                }
                if (nombre != null && !nombre.equals(persona.getNombre())) {
                    persona.setNombre(nombre);
                    updated = true;
                }
                if (rol != null && !rol.equals(persona.getRol())) {
                    persona.setRol(rol);
                    updated = true;
                }
                if (updated) personaRepository.save(persona);
                return persona;
            })
            .orElseGet(() -> {
                // Si no existe la creamos por primera vez
                Persona persona = new Persona();
                persona.setAuth0Id(auth0Id);
                persona.setEmail(email);
                persona.setNombre(nombre);
                persona.setRol(rol);
                return personaRepository.save(persona);
            });
    }
}