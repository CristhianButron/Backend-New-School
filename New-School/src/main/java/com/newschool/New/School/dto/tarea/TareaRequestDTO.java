package com.newschool.New.School.dto.tarea;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TareaRequestDTO {
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaEntrega;
    private Integer cursoId;
}