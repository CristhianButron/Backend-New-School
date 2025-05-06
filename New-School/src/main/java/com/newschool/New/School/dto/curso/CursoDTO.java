package com.newschool.New.School.dto.curso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer docenteId;
    private Integer gradoId;
}