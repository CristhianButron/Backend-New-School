package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Grados;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradoRepository extends JpaRepository<Grados, Integer> {

    Optional<Grados> findByDescripcion(String descripcion);

    // Usando @Query para evitar problemas con el nombre del campo con guión bajo
    @Query("SELECT g FROM Grados g WHERE g.primaria_secundaria = :esPrimaria")
    List<Grados> findByNivel(@Param("esPrimaria") Boolean esPrimaria);
}