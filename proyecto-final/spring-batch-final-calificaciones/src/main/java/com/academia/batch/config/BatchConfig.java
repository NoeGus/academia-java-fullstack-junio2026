package com.academia.batch.config;

import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;
import com.academia.batch.processor.EstudianteProcessor;
import com.academia.batch.processor.ReporteEstudianteProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    // ── STEP 1: CSV → MySQL ──────────────────────────────────────────────────

    @Bean
    public FlatFileItemReader<Estudiante> csvReader() {
        return new FlatFileItemReaderBuilder<Estudiante>()
                .name("csvReader")
                .resource(new ClassPathResource("estudiantes.csv"))
                .delimited()
                .names("nombre", "grupo", "nota1", "nota2", "nota3")
                .targetType(Estudiante.class)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Estudiante> mysqlWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Estudiante>()
                .dataSource(dataSource)
                .sql("INSERT INTO estudiantes_procesados (nombre, grupo, nota1, nota2, nota3, promedio) " +
                        "VALUES (:nombre, :grupo, :nota1, :nota2, :nota3, :promedio)")
                .beanMapped()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      FlatFileItemReader<Estudiante> csvReader,
                      JdbcBatchItemWriter<Estudiante> mysqlWriter) {
        return new StepBuilder("step1", jobRepository)
                .<Estudiante, Estudiante>chunk(10, transactionManager)
                .reader(csvReader)
                .processor(new EstudianteProcessor())
                .writer(mysqlWriter)
                .build();
    }

    // ── STEP 2: MySQL → MongoDB ──────────────────────────────────────────────

    @Bean
    public JdbcCursorItemReader<Estudiante> mysqlReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Estudiante>()
                .name("mysqlReader")
                .dataSource(dataSource)
                .sql("SELECT nombre, grupo, nota1, nota2, nota3, promedio FROM estudiantes_procesados")
                .rowMapper(new DataClassRowMapper<>(Estudiante.class))
                .build();
    }

    @Bean
    public MongoItemWriter<EstudianteReporte> mongoWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<EstudianteReporte>()
                .template(mongoTemplate)
                .collection("reportes_estudiantes")
                .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository,
                      PlatformTransactionManager transactionManager,
                      JdbcCursorItemReader<Estudiante> mysqlReader,
                      MongoItemWriter<EstudianteReporte> mongoWriter) {
        return new StepBuilder("step2", jobRepository)
                .<Estudiante, EstudianteReporte>chunk(10, transactionManager)
                .reader(mysqlReader)
                .processor(new ReporteEstudianteProcessor())
                .writer(mongoWriter)
                .build();
    }

    // ── JOB ─────────────────────────────────────────────────────────────────

    @Bean
    public Job job(JobRepository jobRepository, Step step1, Step step2) {
        return new JobBuilder("calificacionesJob", jobRepository)
                .start(step1)
                .next(step2)
                .build();
    }
}