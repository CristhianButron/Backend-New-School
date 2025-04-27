package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Padres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PadresRepository extends JpaRepository<Padres, Integer> {

    @Query("SELECT p FROM Padres p WHERE p.usuarioIdUsuario.idUsuario = :idUsuario")
    Optional<Padres> findByUsuarioId(@Param("idUsuario") Integer idUsuario);
}