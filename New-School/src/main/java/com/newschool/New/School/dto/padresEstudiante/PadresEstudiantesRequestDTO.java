package com.newschool.New.School.dto.padresEstudiante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PadresEstudiantesRequestDTO {
    private Integer padreId;
    private Integer estudianteId;
}