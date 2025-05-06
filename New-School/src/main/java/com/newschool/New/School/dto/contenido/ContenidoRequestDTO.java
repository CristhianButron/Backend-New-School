package com.newschool.New.School.dto.contenido;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContenidoRequestDTO {
    @NotBlank(message = "El título es requerido")
    private String titulo;
    
    @NotBlank(message = "La descripción es requerida")
    private String descripcion;
    
    @NotBlank(message = "El tipo es requerido")
    private String tipo;
    
    @NotBlank(message = "La URL es requerida")
    private String url;
    
    private LocalDate fechaCreacion;
    
    @NotNull(message = "El ID del curso es requerido")
    private Integer cursoId;
}