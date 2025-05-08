package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Cursos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Cursos, Integer> {
    List<Cursos> findByDocenteIdDocente(Integer docenteId);
    List<Cursos> findByGradoId(Integer gradoId);
    Optional<Cursos> findByNombre(String nombre);
}