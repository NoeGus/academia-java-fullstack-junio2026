package com.academia.batch.controller;

import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;
import com.academia.batch.service.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteRepository repository;
    private final EstudianteService service;

    public EstudianteController(EstudianteRepository repository, EstudianteService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public List<EstudianteEntity> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteEntity> obtener(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/aprobados/total")
    public Map<String, Long> totalAprobados() {
        return Map.of("aprobados", service.contarAprobados());
    }

    @PostMapping
    public ResponseEntity<EstudianteEntity> crear(@RequestBody EstudianteEntity nuevo) {
        EstudianteEntity guardado = repository.save(nuevo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstudianteEntity> actualizar(@PathVariable Long id, @RequestBody EstudianteEntity datos) {
        return repository.findById(id)
                .map(e -> {
                    e.setNombre(datos.getNombre());
                    e.setGrupo(datos.getGrupo());
                    e.setNota1(datos.getNota1());
                    e.setNota2(datos.getNota2());
                    e.setNota3(datos.getNota3());
                    e.setPromedio(datos.getPromedio());
                    return ResponseEntity.ok(repository.save(e));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EstudianteEntity> cambiarGrupo(@PathVariable Long id,
                                                         @RequestBody Map<String, String> cambios) {
        return repository.findById(id)
                .map(e -> {
                    if (cambios.containsKey("grupo")) {
                        e.setGrupo(cambios.get("grupo"));
                    }
                    return ResponseEntity.ok(repository.save(e));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
