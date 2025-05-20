package com.newschool.New.School.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaRequestDTO {
    private String contenido;
    private Integer tareaId;
    private Integer estudianteId;
    private byte[] archivo;
}