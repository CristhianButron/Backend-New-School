package com.newschool.New.School.dto.inscripcion;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para crear una nueva inscripción de grado")
public class InscripcionGradoRequestDTO {

    @Schema(description = "Fecha y hora de la inscripción (opcional, si no se proporciona se usará la fecha actual)")
    private LocalDateTime fechaInscripcion;

    @NotNull(message = "El año de gestión es requerido")
    @Min(value = 2020, message = "El año de gestión debe ser mayor o igual a 2020")
    @Max(value = 2100, message = "El año de gestión debe ser menor o igual a 2100")
    @Schema(description = "Año escolar de la inscripción", example = "2025", required = true)
    private Integer gestion;

    @NotNull(message = "El ID del estudiante es requerido")
    @Schema(description = "ID del estudiante a inscribir", example = "1", required = true)
    private Integer estudianteId;

    @NotNull(message = "El ID del grado es requerido")
    @Schema(description = "ID del grado al que se inscribe el estudiante", example = "1", required = true)
    private Integer gradoId;
}