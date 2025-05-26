package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Tareas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;	
import java.util.Optional;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tareas, Integer> {
    List<Tareas> findByCursoId_curso(Integer cursoId);
    List<Tareas> findByTituloContainingIgnoreCase(String titulo);
    
    // Agregar métodos útiles
    Optional<Tareas> findByTituloAndCursoIdCurso(String titulo, Integer cursoId);
    List<Tareas> findByFecha_entregaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Tareas> findByCursoId_cursoAndFecha_entregaAfter(Integer cursoId, LocalDateTime fecha);
    boolean existsByTituloAndCursoId_curso(String titulo, Integer cursoId);
}