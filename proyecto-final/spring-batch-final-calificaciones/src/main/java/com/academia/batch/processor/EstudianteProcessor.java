package com.academia.batch.processor;

import com.academia.batch.model.Estudiante;
import org.springframework.batch.item.ItemProcessor;

public class EstudianteProcessor implements ItemProcessor<Estudiante, Estudiante> {

    @Override
    public Estudiante process(Estudiante estudiante) {
        double promedio = (estudiante.getNota1() + estudiante.getNota2() + estudiante.getNota3()) / 3.0;
        estudiante.setPromedio(Math.round(promedio * 100.0) / 100.0);
        return estudiante;
    }
}
