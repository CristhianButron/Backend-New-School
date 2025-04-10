package com.newschool.New.School.dto.padre;

import com.newschool.New.School.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PadreResponseDTO {
    private Integer id;
    private String parentesco;
    private Integer usuarioId;
    private UsuarioDTO usuario;
}