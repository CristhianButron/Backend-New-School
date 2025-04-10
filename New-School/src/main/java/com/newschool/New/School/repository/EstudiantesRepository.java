package com.newschool.New.School.repository;


import com.newschool.New.School.entity.Docentes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.newschool.New.School.entity.Estudiantes;

import java.util.Optional;

@Repository
public interface EstudiantesRepository extends JpaRepository<Estudiantes, Integer> {

    @Query("SELECT e FROM Estudiantes e WHERE e.usuarioIdUsuario.idUsuario = :idUsuario")
    Optional<Estudiantes> findByUsuarioId(@Param("idUsuario") Integer idUsuario);

}
