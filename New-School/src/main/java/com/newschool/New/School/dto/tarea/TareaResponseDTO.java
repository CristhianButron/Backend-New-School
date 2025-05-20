package com.newschool.New.School.dto.tarea;

import com.newschool.New.School.dto.curso.CursoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TareaResponseDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEntrega;
    private CursoDTO curso;
}