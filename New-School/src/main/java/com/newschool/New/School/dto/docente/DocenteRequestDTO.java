package com.newschool.New.School.dto.docente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocenteRequestDTO {
    private Integer id;
    private String licenciatura;
    private Integer usuarioId;
    private byte[] titulo;
}