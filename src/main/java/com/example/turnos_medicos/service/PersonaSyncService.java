package com.example.turnos_medicos.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.turnos_medicos.entity.Persona;
import com.example.turnos_medicos.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.List;

@Service
public class PersonaSyncService {
    private final String auth0Domain = "dev-2yrckbrmnb1o4m1v.us.auth0.com";
    // @Autowired
    // private Auth0TokenService auth0TokenService;
    @Autowired
    private PersonaRepository personaRepository;

    public Persona syncPersonaFromJwt(String jwtToken) {
        DecodedJWT jwt = JWT.decode(jwtToken.replace("Bearer ", ""));

        String auth0Id = jwt.getSubject(); // sub claim
        String email = jwt.getClaim("email").asString();
        String nombre = jwt.getClaim("name").asString();
        // Extraer el rol desde el claim personalizado de Auth0
        List<String> roles = jwt.getClaim("https://turnosmedicos.com/roles").asList(String.class);
        String rol = (roles != null && !roles.isEmpty()) ? roles.get(0) : "USUARIO";

        // Buscar o actualizar en la base de datos
        return personaRepository.findByAuth0Id(auth0Id)
            .map(persona -> {
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
                Persona persona = new Persona();
                persona.setAuth0Id(auth0Id);
                persona.setEmail(email);
                persona.setNombre(nombre);
                persona.setRol(rol);
                return personaRepository.save(persona);
            });
    }
} 