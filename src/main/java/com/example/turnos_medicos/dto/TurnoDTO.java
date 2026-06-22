package com.example.turnos_medicos.dto;

import java.time.LocalDateTime;

public class TurnoDTO {
    private Long id;
    private String fecha;
    private String horario;
    private String disponible;
    private String ubicacion;
    private String customerEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TurnoDTO() {}

    public TurnoDTO(Long id, String fecha, String horario, String disponible, String ubicacion, String customerEmail, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fecha = fecha;
        this.horario = horario;
        this.disponible = disponible;
        this.ubicacion = ubicacion;
        this.customerEmail = customerEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public String getDisponible() { return disponible; }
    public void setDisponible(String disponible) { this.disponible = disponible; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}