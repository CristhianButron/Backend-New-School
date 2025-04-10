package com.newschool.New.School.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO {
    private String ci;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String rol;
}