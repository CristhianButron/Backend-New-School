package com.newschool.New.School.dto.inscripcion;

import java.time.LocalDateTime;

import com.newschool.New.School.dto.estudiante.EstudianteResponseDTO;
import com.newschool.New.School.dto.grado.GradoDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta detallada de inscripción a grado")
public class InscripcionGradoResponseDTO {

    @Schema(description = "Identificador único de la inscripción")
    private Integer id;

    @Schema(description = "Fecha y hora de la inscripción")
    private LocalDateTime fechaInscripcion;

    @Schema(description = "Año escolar de la inscripción")
    private Integer gestion;

    @Schema(description = "Información del estudiante inscrito")
    private EstudianteResponseDTO estudiante;

    @Schema(description = "Información del grado al que se inscribe")
    private GradoDTO grado;

    @Schema(description = "Estado de la inscripción")
    private String estado = "ACTIVO";
}