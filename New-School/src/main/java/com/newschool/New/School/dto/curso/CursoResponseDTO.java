package com.newschool.New.School.dto.curso;

import com.newschool.New.School.dto.docente.DocenteDTO;
import com.newschool.New.School.dto.grado.GradoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursoResponseDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private DocenteDTO docente;
    private GradoDTO grado;
}