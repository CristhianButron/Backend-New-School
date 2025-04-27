package com.newschool.New.School.dto.docente;

import com.newschool.New.School.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocenteResponseDTO {
    private Integer id;
    private String licenciatura;
    private Integer usuarioId;
    private boolean tieneTitulo;
    private UsuarioDTO usuario;
}