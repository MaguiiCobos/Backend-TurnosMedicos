package com.example.turnos_medicos.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "menu_items")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre_medico;
    private String especialidad;
    private String description;
    private Double price;
    private String image;
    private Boolean estado;  // true -> disponible, false -> no disponible

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Turno> turnos;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return nombre_medico; }
    public void setName(String nombre_medico) { this.nombre_medico = nombre_medico; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public List<Turno> getTurnos() { return turnos; }
    public void setTurnos(List<Turno> turnos) { this.turnos = turnos; }
} 
