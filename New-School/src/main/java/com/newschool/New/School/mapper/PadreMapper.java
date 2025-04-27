package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.padre.PadreDTO;
import com.newschool.New.School.dto.UsuarioDTO;
import com.newschool.New.School.entity.Padres;
import com.newschool.New.School.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PadreMapper {

    @Autowired
    private UsuarioMapper usuarioMapper;

    /**
     * Convierte una entidad Padre a un DTO
     */
    public PadreDTO toDTO(Padres padre) {
        if (padre == null) {
            return null;
        }

        PadreDTO dto = new PadreDTO();
        dto.setId(padre.getIdPadre());
        dto.setParentesco(padre.getParentesco());

        if (padre.getUsuarioIdUsuario() != null) {
            dto.setUsuarioId(padre.getUsuarioIdUsuario().getIdUsuario());
            dto.setUsuario(usuarioMapper.toDTO(padre.getUsuarioIdUsuario()));
        }

        return dto;
    }

    /**
     * Convierte un DTO a una entidad Padre
     */
    public Padres toEntity(PadreDTO dto, Usuario usuario) {
        if (dto == null) {
            return null;
        }

        Padres padre = new Padres();
        padre.setIdPadre(dto.getId());
        padre.setParentesco(dto.getParentesco());
        padre.setUsuarioIdUsuario(usuario);

        return padre;
    }

    /**
     * Convierte un Usuario a un UsuarioDTO
     */
    public UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return usuarioMapper.toDTO(usuario);
    }
}