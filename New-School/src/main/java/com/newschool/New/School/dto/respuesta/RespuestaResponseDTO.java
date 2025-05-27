package com.newschool.New.School.dto.respuesta;

import com.newschool.New.School.dto.estudiante.EstudianteDTO;
import com.newschool.New.School.dto.tarea.TareaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaResponseDTO {
    private Integer id;
    private String respuesta;
    private String archivo;
    private int puntaje;
    private LocalDate fechaEntrega;
    private TareaDTO tarea;
    private EstudianteDTO estudiante;
    private boolean tieneArchivo;
}