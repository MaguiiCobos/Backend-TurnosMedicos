package com.example.turnos_medicos.model;

import jakarta.persistence.*;

@Entity
public class Turno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fecha;
    private String hora;
    private boolean disponible;
    private String pacienteEmail;

    public Turno() {}

    public Turno(String fecha, String hora, boolean disponible) {
        this.fecha = fecha;
        this.hora = hora;
        this.disponible = disponible;
    }

    // getters y setters
    public Long getId() { return id; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getPacienteEmail() { return pacienteEmail; }
    public void setPacienteEmail(String pacienteEmail) { this.pacienteEmail = pacienteEmail; }
}