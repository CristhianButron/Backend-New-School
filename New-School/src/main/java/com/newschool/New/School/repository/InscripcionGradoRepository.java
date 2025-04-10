package com.newschool.New.School.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.newschool.New.School.entity.Inscripcion_grados;

@Repository
public interface InscripcionGradoRepository extends JpaRepository<Inscripcion_grados, Integer> {

    /**
     * Busca todas las inscripciones de un estudiante
     */
    @Query("SELECT i FROM Inscripcion_grados i WHERE i.estudiante.idEstudiante = :estudianteId")
    List<Inscripcion_grados> findByEstudianteIdEstudiante(@Param("estudianteId") Integer estudianteId);

    /**
     * Busca todas las inscripciones para un grado específico
     */
    @Query("SELECT i FROM Inscripcion_grados i WHERE i.grado.id = :gradoId")
    List<Inscripcion_grados> findByGradoId(@Param("gradoId") Integer gradoId);

    /**
     * Busca todas las inscripciones de un año de gestión específico
     */
    List<Inscripcion_grados> findByGestion(Integer gestion);

    /**
     * Busca una inscripción específica por estudiante, grado y gestión
     */
    @Query("SELECT i FROM Inscripcion_grados i WHERE i.estudiante.idEstudiante = :estudianteId AND i.grado.id = :gradoId AND i.gestion = :gestion")
    Optional<Inscripcion_grados> findByEstudianteIdEstudianteAndGradoIdAndGestion(
            @Param("estudianteId") Integer estudianteId,
            @Param("gradoId") Integer gradoId,
            @Param("gestion") Integer gestion);

    /**
     * Verifica si ya existe una inscripción para un estudiante en un año de gestión
     */
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Inscripcion_grados i WHERE i.estudiante.idEstudiante = :estudianteId AND i.gestion = :gestion")
    boolean existsByEstudianteIdEstudianteAndGestion(
            @Param("estudianteId") Integer estudianteId,
            @Param("gestion") Integer gestion);

    /**
     * Cuenta el número de estudiantes inscritos en un grado específico para una gestión
     */
    @Query("SELECT COUNT(i) FROM Inscripcion_grados i WHERE i.grado.id = :gradoId AND i.gestion = :gestion")
    long countEstudiantesByGradoAndGestion(@Param("gradoId") Integer gradoId, @Param("gestion") Integer gestion);
}