package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Docentes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


@Repository
public interface DocentesRepository extends JpaRepository<Docentes, Integer> {

    @Query("SELECT d FROM Docentes d WHERE d.usuarioIdUsuario.idUsuario = :idUsuario")
    Optional<Docentes> findByUsuarioId(@Param("idUsuario") Integer idUsuario);
}