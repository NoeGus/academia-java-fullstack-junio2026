package com.academia.batch.controller;

import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;
import com.academia.batch.service.EstudianteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstudianteController.class)
class EstudianteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EstudianteRepository repository;

    @MockBean
    private EstudianteService service;

    @Test
    void debeObtenerEstudiantePorId() throws Exception {
        when(repository.findById(1L)).thenReturn(Optional.of(nuevo(1L, "Juan Perez", "A", 80, 75, 90, 81.67)));

        mockMvc.perform(get("/api/estudiantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Perez"))
                .andExpect(jsonPath("$.grupo").value("A"));
    }

    @Test
    void debeCambiarElGrupoDelEstudiante() throws Exception {
        EstudianteEntity actual = nuevo(1L, "Juan Perez", "A", 80, 75, 90, 81.67);
        EstudianteEntity actualizado = nuevo(1L, "Juan Perez", "B", 80, 75, 90, 81.67);

        when(repository.findById(1L)).thenReturn(Optional.of(actual));
        when(repository.save(any(EstudianteEntity.class))).thenReturn(actualizado);

        mockMvc.perform(patch("/api/estudiantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("grupo", "B"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grupo").value("B"));
    }

    @Test
    void debeEliminarEstudiante() throws Exception {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        mockMvc.perform(delete("/api/estudiantes/1"))
                .andExpect(status().isNoContent());

        verify(repository).deleteById(1L);
    }

    @Test
    void debeEditarEstudiante() throws Exception {
        EstudianteEntity existente = nuevo(1L, "Juan Perez", "A", 80, 75, 90, 81.67);
        EstudianteEntity modificado = nuevo(1L, "Juan Perez Editado", "C", 90, 85, 95, 90.0);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any(EstudianteEntity.class))).thenReturn(modificado);

        mockMvc.perform(put("/api/estudiantes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modificado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Perez Editado"))
                .andExpect(jsonPath("$.grupo").value("C"))
                .andExpect(jsonPath("$.promedio").value(90.0));
    }

    @Test
    void debeAgregarEstudiante() throws Exception {
        EstudianteEntity nuevo = nuevo(2L, "Laura Diaz", "C", 85, 90, 80, 85.0);

        when(repository.save(any(EstudianteEntity.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/estudiantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Laura Diaz"))
                .andExpect(jsonPath("$.grupo").value("C"));
    }

    private EstudianteEntity nuevo(Long id, String nombre, String grupo,
                                   double nota1, double nota2, double nota3, double promedio) {
        EstudianteEntity e = new EstudianteEntity();
        e.setId(id);
        e.setNombre(nombre);
        e.setGrupo(grupo);
        e.setNota1(nota1);
        e.setNota2(nota2);
        e.setNota3(nota3);
        e.setPromedio(promedio);
        return e;
    }
}
