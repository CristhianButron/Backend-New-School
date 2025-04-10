package com.newschool.New.School.dto.estudiante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteRequestDTO {
    private Integer id;
    private String fechaNacimiento;
    private Integer usuarioId;
    private byte[] certificadoNacimiento;
}