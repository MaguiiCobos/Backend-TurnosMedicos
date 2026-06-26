package com.example.turnos_medicos.service;

import com.example.turnos_medicos.entity.Medico;
import com.example.turnos_medicos.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {
    @Autowired
    private MedicoRepository medicoRepository;

    public List<Medico> obtenerTodos() {
        return medicoRepository.findAll();
    }

    public Optional<Medico> obtenerPorId(Long id) {
        return medicoRepository.findById(id);
    }

    public List<Medico> obtenerPorEspecialidad(String especialidad) {
        return medicoRepository.findByEspecialidad(especialidad);
    }

    public Medico guardarMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    public void eliminarMedico(Long id) {
        medicoRepository.deleteById(id);
    }
}
