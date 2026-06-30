package com.academia.batch.processor;

import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;
import org.springframework.batch.item.ItemProcessor;

public class ReporteEstudianteProcessor implements ItemProcessor<Estudiante, EstudianteReporte> {

    @Override
    public EstudianteReporte process(Estudiante estudiante) {
        EstudianteReporte reporte = new EstudianteReporte();
        reporte.setNombre(estudiante.getNombre());
        reporte.setGrupo(estudiante.getGrupo());
        reporte.setPromedio(estudiante.getPromedio());
        reporte.setEstado(estudiante.getPromedio() >= 70 ? "APROBADO" : "REPROBADO");
        return reporte;
    }
}