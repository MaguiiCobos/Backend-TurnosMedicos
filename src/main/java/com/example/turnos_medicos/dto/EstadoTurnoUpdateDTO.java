package com.example.turnos_medicos.dto;

import com.example.turnos_medicos.entity.Turno.EstadoTurno;

public class EstadoTurnoUpdateDTO {
    private EstadoTurno estado;

    public EstadoTurnoUpdateDTO() {}
    
    public EstadoTurnoUpdateDTO(EstadoTurno estado) {
        this.estado = estado;
    }

    public EstadoTurno getEstado() { return estado; }
    public void setEstado(EstadoTurno estado) { this.estado = estado; }
}