package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Respuestas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuestas, Integer> {
    List<Respuestas> findByTareaId(Integer tareaId);
    List<Respuestas> findByEstudianteIdEstudiante(Integer estudianteId);
    List<Respuestas> findByTareaIdAndEstudianteIdEstudiante(Integer tareaId, Integer estudianteId);
}