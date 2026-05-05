package com.example.turnos_medicos;

import com.example.turnos_medicos.model.Turno;
import com.example.turnos_medicos.repository.TurnoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(TurnoRepository repo) {
        return args -> {
            repo.save(new Turno("2026-06-01", "10:00", true));
            repo.save(new Turno("2026-06-01", "11:00", true));
            repo.save(new Turno("2026-06-01", "12:00", true));
        };
    }
}