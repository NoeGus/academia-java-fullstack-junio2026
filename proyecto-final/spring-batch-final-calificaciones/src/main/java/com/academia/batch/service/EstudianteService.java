package com.academia.batch.service;

import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public List<EstudianteEntity> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<EstudianteEntity> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    public EstudianteEntity crear(EstudianteEntity estudiante) {
        return repository.save(estudiante);
    }

    public EstudianteEntity reemplazar(Long id, EstudianteEntity estudiante) {
        estudiante.setId(id);
        return repository.save(estudiante);
    }

    public EstudianteEntity actualizar(Long id, EstudianteEntity parcial) {
        EstudianteEntity existente = repository.findById(id).orElseThrow();
        if (parcial.getNombre() != null) existente.setNombre(parcial.getNombre());
        if (parcial.getGrupo()  != null) existente.setGrupo(parcial.getGrupo());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public long contarAprobados() {
        return repository.findAll().stream()
                .filter(e -> e.getPromedio() >= 70)
                .count();
    }
}
