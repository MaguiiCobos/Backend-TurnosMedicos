package com.example.turnos_medicos.dto;

import com.example.turnos_medicos.entity.Turno.EstadoTurno; 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
public class TurnoDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime horario;
    private EstadoTurno estado; 
    private String ubicacion;
    private String customerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TurnoDTO() {}

    public TurnoDTO(Long id, LocalDate fecha, LocalTime horario, EstadoTurno estado, String ubicacion, String customerEmail, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fecha = fecha;
        this.horario = horario;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.customerEmail = customerEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }
    public EstadoTurno getEstado() { return estado; }
    public void setEstado(EstadoTurno estado) { this.estado = estado; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}