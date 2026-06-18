package com.example.turnos_medicos.repository;

import com.example.turnos_medicos.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    // Métodos personalizados si se necesitan
}
