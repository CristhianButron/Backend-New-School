package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.estudiante.EstudianteDTO;
import com.newschool.New.School.dto.UsuarioDTO;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstudianteMapper {

    @Autowired
    private UsuarioMapper usuarioMapper;

    /**
     * Convierte una entidad Estudiante a un DTO
     */
    public EstudianteDTO toDTO(Estudiantes estudiante) {
        if (estudiante == null) {
            return null;
        }

        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(estudiante.getIdEstudiante());
        dto.setFechaNacimiento(estudiante.getFechaNacimiento());
        dto.setCertificadoNacimiento(estudiante.getCertificadoNacimiento());

        if (estudiante.getUsuarioIdUsuario() != null) {
            dto.setUsuarioId(estudiante.getUsuarioIdUsuario().getIdUsuario());
            dto.setUsuario(usuarioMapper.toDTO(estudiante.getUsuarioIdUsuario()));
        }

        return dto;
    }

    /**
     * Convierte un DTO a una entidad Estudiante
     */
    public Estudiantes toEntity(EstudianteDTO dto, Usuario usuario) {
        if (dto == null) {
            return null;
        }

        Estudiantes estudiante = new Estudiantes();
        estudiante.setIdEstudiante(dto.getId());
        estudiante.setFechaNacimiento(dto.getFechaNacimiento());
        estudiante.setCertificadoNacimiento(dto.getCertificadoNacimiento());
        estudiante.setUsuarioIdUsuario(usuario);

        return estudiante;
    }

    /**
     * Convierte un Usuario a un UsuarioDTO
     */
    public UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return usuarioMapper.toDTO(usuario);
    }
}