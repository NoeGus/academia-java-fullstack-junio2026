package com.academia.batch.service;

import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public long contarAprobados() {
        return repository.findAll().stream()
                .filter(e -> e.getPromedio() >= 70)
                .count();
    }
}
