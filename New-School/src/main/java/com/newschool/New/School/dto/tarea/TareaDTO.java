package com.newschool.New.School.dto.tarea;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TareaDTO {
    private Integer id_tarea; // Cambiado de id a id_tarea para coincidir con la entidad
    private String titulo;
    private String descripcion;
    private String archivo;
    private String fecha_entrega;
    private int puntaje_maximo;
    private Integer cursoId; // Representa el id del curso asociado
}