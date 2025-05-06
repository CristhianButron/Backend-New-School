package com.newschool.New.School.dto.contenido;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContenidoDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String url;
    private LocalDate fechaCreacion;
    private Integer cursoId;
}