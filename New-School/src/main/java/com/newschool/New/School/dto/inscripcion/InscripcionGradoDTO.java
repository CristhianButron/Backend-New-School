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
@Schema(description = "Información de inscripción de un estudiante a un grado")
public class InscripcionGradoDTO {

    @Schema(description = "Identificador único de la inscripción", hidden = true)
    private Integer id;

    @Schema(description = "Fecha y hora de la inscripción", example = "2025-03-01T14:30:00")
    private LocalDateTime fechaInscripcion;

    @Schema(description = "Año escolar de la inscripción", example = "2025")
    private Integer gestion;

    @Schema(description = "ID del estudiante inscrito", example = "1")
    private Integer estudianteId;

    @Schema(description = "ID del grado al que se inscribe", example = "1")
    private Integer gradoId;

    @Schema(description = "Información detallada del estudiante")
    private EstudianteResponseDTO estudiante;

    @Schema(description = "Información detallada del grado")
    private GradoDTO grado;
}
