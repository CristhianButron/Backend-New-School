package com.newschool.New.School.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    private String ci;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String rol;

    // Mapa para datos específicos según el rol
    // Para Admin: "cargo"
    // Para Estudiante: "fechaNacimiento"
    // Para Docente: "licenciatura"
    // Para Padre: "parentesco"
    private Map<String, String> datosEspecificos;
}