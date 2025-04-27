package com.newschool.New.School.dto.estudiante;

import com.newschool.New.School.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteDTO {
    private Integer id;
    private String fechaNacimiento;
    private Integer usuarioId;
    private byte[] certificadoNacimiento;
    private UsuarioDTO usuario;
}