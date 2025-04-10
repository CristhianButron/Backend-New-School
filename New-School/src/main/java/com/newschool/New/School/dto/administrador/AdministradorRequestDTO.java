package com.newschool.New.School.dto.administrador;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministradorRequestDTO {
    private Integer id;
    private String cargo;
    private Integer usuarioId;
}