package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Tareas;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tareas, Integer> {
  
    // Método básico para buscar por título
    List<Tareas> findByTituloContainingIgnoreCase(String titulo);
}