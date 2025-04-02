package com.newschool.New.School.dto.grado;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información de un grado escolar")
public class GradoDTO {

    @Schema(description = "Identificador único del grado", example = "1")
    private Integer id;

    @Schema(description = "Nombre o descripción del grado", example = "Primer Grado de Primaria")
    private String descripcion;

    @Schema(description = "Indica si el grado es de primaria (true) o secundaria (false)", example = "true")
    private Boolean primariaSencundaria;
}