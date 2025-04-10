package com.newschool.New.School.dto.administrador;


import com.newschool.New.School.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorDTO {
    private Integer id;
    private String cargo;
    private Integer usuarioId;
    private UsuarioDTO usuario;
}