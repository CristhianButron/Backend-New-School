package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.UsuarioDTO;
import com.newschool.New.School.dto.administrador.AdministradorDTO;
import com.newschool.New.School.dto.administrador.AdministradorRequestDTO;
import com.newschool.New.School.dto.administrador.AdministradorResponseDTO;

import com.newschool.New.School.entity.Administrativos;
import com.newschool.New.School.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdministradorMapper {

    @Autowired
    private UsuarioMapper usuarioMapper;

    /**
     * Convierte una entidad Administrativos a un DTO
     */
    public AdministradorDTO toDTO(Administrativos administrador) {
        if (administrador == null) {
            return null;
        }

        AdministradorDTO dto = new AdministradorDTO();
        dto.setId(administrador.getIdAdministrativo());
        dto.setCargo(administrador.getCargo());

        if (administrador.getUsuarioIdUsuario() != null) {
            dto.setUsuarioId(administrador.getUsuarioIdUsuario().getIdUsuario());
            dto.setUsuario(usuarioMapper.toDTO(administrador.getUsuarioIdUsuario()));
        }

        return dto;
    }

    /**
     * Convierte un RequestDTO a una entidad Administrativos
     */
    public Administrativos toEntity(AdministradorRequestDTO dto, Usuario usuario) {
        if (dto == null) {
            return null;
        }

        Administrativos administrador = new Administrativos();
        if (dto.getId() != null) {
            administrador.setIdAdministrativo(dto.getId());
        }
        administrador.setCargo(dto.getCargo());
        administrador.setUsuarioIdUsuario(usuario);

        return administrador;
    }

    /**
     * Convierte una entidad Administrativos a un ResponseDTO
     */
    public AdministradorResponseDTO toResponseDTO(Administrativos administrador) {
        if (administrador == null) {
            return null;
        }

        AdministradorResponseDTO responseDTO = new AdministradorResponseDTO();
        responseDTO.setId(administrador.getIdAdministrativo());
        responseDTO.setCargo(administrador.getCargo());

        if (administrador.getUsuarioIdUsuario() != null) {
            responseDTO.setUsuarioId(administrador.getUsuarioIdUsuario().getIdUsuario());
            responseDTO.setUsuario(usuarioMapper.toDTO(administrador.getUsuarioIdUsuario()));
        }

        return responseDTO;
    }

    /**
     * Actualiza una entidad existente con datos de un RequestDTO
     */
    public void updateEntityFromDTO(AdministradorRequestDTO dto, Administrativos administrador) {
        if (dto == null || administrador == null) {
            return;
        }

        if (dto.getCargo() != null) {
            administrador.setCargo(dto.getCargo());
        }
    }

    /**
     * Convierte un Usuario a un UsuarioDTO
     */
    public UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return usuarioMapper.toDTO(usuario);
    }
}