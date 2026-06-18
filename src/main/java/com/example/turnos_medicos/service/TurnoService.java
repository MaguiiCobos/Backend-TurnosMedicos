package com.example.turnos_medicos.service;

import com.example.turnos_medicos.entity.Turno;
import com.example.turnos_medicos.repository.TurnoRepository;
import com.example.turnos_medicos.service.MenuItemService;
import com.example.turnos_medicos.entity.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {
    private final TurnoRepository turnoRepository;
    private final MenuItemService menuItemService;

    @Autowired
    public TurnoService(TurnoRepository turnoRepository, MenuItemService menuItemService) {
        this.turnoRepository = turnoRepository;
        this.menuItemService = menuItemService;
    }

    public Turno save(Turno turno) {
        double total = 0.0;
        if (turno.getItems() != null) {
            for (var item : turno.getItems()) {
                item.setTurno(turno);
                if (item.getMenuItem() != null && item.getMenuItem().getId() != null) {
                    MenuItem menuItem = menuItemService.findById(item.getMenuItem().getId())
                        .orElseThrow(() -> new RuntimeException("MenuItem no encontrado"));
                    item.setMenuItem(menuItem);
                    item.setPrice(menuItem.getPrice()); // Setea el precio real
                    total += menuItem.getPrice() * item.getQuantity();
                }
            }
        }
        turno.setTotal(total);
        return turnoRepository.save(turno);
    }

    public List<Turno> findAll() {
        return turnoRepository.findAll();
    }

    public Optional<Turno> findById(Long id) {
        return turnoRepository.findById(id);
    }

    public List<Turno> findByUserId(String userId) {
        return turnoRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        turnoRepository.deleteById(id);
    }
} 
