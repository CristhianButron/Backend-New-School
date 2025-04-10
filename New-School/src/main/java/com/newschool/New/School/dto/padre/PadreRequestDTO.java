package com.newschool.New.School.dto.padre;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PadreRequestDTO {
    private Integer id;
    private String parentesco;
    private Integer usuarioId;
}