package com.newschool.New.School.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private Integer id;
    private String ci;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
}