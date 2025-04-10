package com.newschool.New.School.repository;

import com.newschool.New.School.entity.Administrativos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrativosRepository extends JpaRepository<Administrativos, Integer> {

    @Query("SELECT a FROM Administrativos a WHERE a.usuarioIdUsuario.idUsuario = :idUsuario")
    Optional<Administrativos> findByUsuarioId(@Param("idUsuario") Integer idUsuario);
}