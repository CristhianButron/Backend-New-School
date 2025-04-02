package com.newschool.New.School.dto.grado ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradoDTO {
    private Integer id;
    private String descripcion;
    private Boolean primariaSencundaria;
}