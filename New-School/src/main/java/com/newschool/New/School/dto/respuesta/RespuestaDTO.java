package com.newschool.New.School.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaDTO {
    private Integer id;
    private String respuesta;
    private String archivo;
    private int puntaje;
    private LocalDate fechaEntrega;
    private Integer estudianteId;
    private Integer tareaId;
}