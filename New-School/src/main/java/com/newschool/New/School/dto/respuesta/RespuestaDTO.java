package com.newschool.New.School.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaDTO {
    private Integer id;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private Integer tareaId;
    private Integer estudianteId;
    private byte[] archivo;
}