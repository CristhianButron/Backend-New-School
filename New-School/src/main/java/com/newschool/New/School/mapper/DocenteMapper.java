package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.docente.DocenteDTO;
import com.newschool.New.School.dto.UsuarioDTO;

import com.newschool.New.School.entity.Docentes;
import com.newschool.New.School.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocenteMapper {

    @Autowired
    private UsuarioMapper usuarioMapper;

    /**
     * Convierte una entidad Docente a un DTO
     */
    public DocenteDTO toDTO(Docentes docente) {
        if (docente == null) {
            return null;
        }

        DocenteDTO dto = new DocenteDTO();
        dto.setId(docente.getIdDocente());
        dto.setLicenciatura(docente.getLicenciatura());
        dto.setTitulo(docente.getTitulo());

        if (docente.getUsuarioIdUsuario() != null) {
            dto.setUsuarioId(docente.getUsuarioIdUsuario().getIdUsuario());
            dto.setUsuario(usuarioMapper.toDTO(docente.getUsuarioIdUsuario()));
        }

        return dto;
    }

    /**
     * Convierte un DTO a una entidad Docente
     */
    public Docentes toEntity(DocenteDTO dto, Usuario usuario) {
        if (dto == null) {
            return null;
        }

        Docentes docente = new Docentes();
        docente.setIdDocente(dto.getId());
        docente.setLicenciatura(dto.getLicenciatura());
        docente.setTitulo(dto.getTitulo());
        docente.setUsuarioIdUsuario(usuario);

        return docente;
    }

    /**
     * Convierte un Usuario a un UsuarioDTO
     */
    public UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return usuarioMapper.toDTO(usuario);
    }
}