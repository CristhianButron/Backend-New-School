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
public class EstudianteResponseDTO {
    private Integer id;
    private String fechaNacimiento;
    private Integer usuarioId;
    private boolean tieneCertificado;
    private UsuarioDTO usuario;
}