package com.newschool.New.School.dto.contenido;

import java.time.LocalDate;
import com.newschool.New.School.dto.curso.CursoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContenidoResponseDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private String tipo;
    private String url;
    private LocalDate fechaCreacion;
    private CursoResponseDTO curso;
}