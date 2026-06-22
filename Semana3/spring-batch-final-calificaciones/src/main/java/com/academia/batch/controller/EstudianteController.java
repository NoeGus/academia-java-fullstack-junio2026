package com.academia.batch.controller;

import com.academia.batch.model.EstudianteReporte;
import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.ReporteRepository;
import com.academia.batch.service.EstudianteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EstudianteController {

    private final EstudianteService service;
    private final ReporteRepository reporteRepository;

    public EstudianteController(EstudianteService service, ReporteRepository reporteRepository) {
        this.service = service;
        this.reporteRepository = reporteRepository;
    }

    // GET todos
    @GetMapping("/estudiantes")
    public List<EstudianteEntity> obtenerTodos() {
        return service.obtenerTodos();
    }

    // GET por id
    @GetMapping("/estudiantes/{id}")
    public ResponseEntity<EstudianteEntity> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST crear
    @PostMapping("/estudiantes")
    public ResponseEntity<EstudianteEntity> crear(@RequestBody EstudianteEntity estudiante) {
        return ResponseEntity.status(201).body(service.crear(estudiante));
    }

    // PUT reemplazar
    @PutMapping("/estudiantes/{id}")
    public ResponseEntity<EstudianteEntity> reemplazar(@PathVariable Long id,
                                                       @RequestBody EstudianteEntity estudiante) {
        return ResponseEntity.ok(service.reemplazar(id, estudiante));
    }

    // PATCH actualizar parcial
    @PatchMapping("/estudiantes/{id}")
    public ResponseEntity<EstudianteEntity> actualizar(@PathVariable Long id,
                                                       @RequestBody EstudianteEntity parcial) {
        return ResponseEntity.ok(service.actualizar(id, parcial));
    }

    // DELETE eliminar
    @DeleteMapping("/estudiantes/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // GET total aprobados
    @GetMapping("/estudiantes/aprobados/total")
    public ResponseEntity<Long> totalAprobados() {
        return ResponseEntity.ok(service.contarAprobados());
    }

    // GET reportes MongoDB
    @GetMapping("/reportes")
    public List<EstudianteReporte> obtenerReportes() {
        return reporteRepository.findAll();
    }

    // GET reportes por estado
    @GetMapping("/reportes/estado/{estado}")
    public List<EstudianteReporte> obtenerPorEstado(@PathVariable String estado) {
        return reporteRepository.findByEstado(estado);
    }
}