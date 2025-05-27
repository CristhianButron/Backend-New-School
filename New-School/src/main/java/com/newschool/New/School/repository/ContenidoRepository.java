package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Contenidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenidos, Integer> {
    List<Contenidos> findByTipo(String tipo);
    List<Contenidos> findByTituloContainingIgnoreCase(String titulo);
}