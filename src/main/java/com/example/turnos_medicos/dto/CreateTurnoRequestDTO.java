package com.example.turnos_medicos.dto;

import java.time.LocalDate;
import java.time.LocalTime;
public class CreateTurnoRequestDTO {
    private LocalDate fecha;
    private LocalTime horario;
    private String ubicacion;
    private Long medicoId;

    public CreateTurnoRequestDTO() {}

    public CreateTurnoRequestDTO(LocalDate fecha, LocalTime horario, String ubicacion, Long medicoId) {
        this.fecha = fecha;
        this.horario = horario;
        this.ubicacion = ubicacion;
        this.medicoId = medicoId;
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }
}
