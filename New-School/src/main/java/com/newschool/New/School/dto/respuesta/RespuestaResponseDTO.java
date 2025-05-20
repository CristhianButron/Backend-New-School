package com.newschool.New.School.dto.respuesta;

import com.newschool.New.School.dto.estudiante.EstudianteDTO;
import com.newschool.New.School.dto.tarea.TareaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaResponseDTO {
    private Integer id;
    private String contenido;
    private LocalDateTime fechaEnvio;
    private TareaDTO tarea;
    private EstudianteDTO estudiante;
    private boolean tieneArchivo;
}