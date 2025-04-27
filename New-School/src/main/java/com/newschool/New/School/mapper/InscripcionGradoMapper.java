package com.newschool.New.School.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newschool.New.School.dto.estudiante.EstudianteDTO;
import com.newschool.New.School.dto.estudiante.EstudianteResponseDTO;
import com.newschool.New.School.dto.inscripcion.InscripcionGradoDTO;
import com.newschool.New.School.dto.inscripcion.InscripcionGradoRequestDTO;
import com.newschool.New.School.dto.inscripcion.InscripcionGradoResponseDTO;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Grados;
import com.newschool.New.School.entity.Inscripcion_grados;

@Component
public class InscripcionGradoMapper {

    private final EstudianteMapper estudianteMapper;
    private final GradoMapper gradoMapper;

    @Autowired
    public InscripcionGradoMapper(EstudianteMapper estudianteMapper, GradoMapper gradoMapper) {
        this.estudianteMapper = estudianteMapper;
        this.gradoMapper = gradoMapper;
    }

    public InscripcionGradoDTO toDTO(Inscripcion_grados inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        // Crear un EstudianteResponseDTO manualmente ya que no existe toResponseDTO en EstudianteMapper
        EstudianteDTO estudianteDTO = estudianteMapper.toDTO(inscripcion.getEstudiante());
        EstudianteResponseDTO estudianteResponseDTO = null;

        if (estudianteDTO != null) {
            estudianteResponseDTO = new EstudianteResponseDTO();
            estudianteResponseDTO.setId(estudianteDTO.getId());
            estudianteResponseDTO.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
            estudianteResponseDTO.setTieneCertificado(estudianteDTO.getCertificadoNacimiento() != null &&
                    estudianteDTO.getCertificadoNacimiento().length > 0);
            estudianteResponseDTO.setUsuarioId(estudianteDTO.getUsuarioId());
            estudianteResponseDTO.setUsuario(estudianteDTO.getUsuario());
        }

        return InscripcionGradoDTO.builder()
                .id(inscripcion.getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .gestion(inscripcion.getGestion())
                .estudianteId(inscripcion.getEstudiante().getIdEstudiante())
                .gradoId(inscripcion.getGrado().getId())
                .estudiante(estudianteResponseDTO)
                .grado(gradoMapper.toDTO(inscripcion.getGrado()))
                .build();
    }

    public List<InscripcionGradoDTO> toDTOList(List<Inscripcion_grados> inscripciones) {
        if (inscripciones == null) {
            return List.of();
        }

        return inscripciones.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public InscripcionGradoResponseDTO toResponseDTO(Inscripcion_grados inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        // Crear un EstudianteResponseDTO manualmente
        EstudianteDTO estudianteDTO = estudianteMapper.toDTO(inscripcion.getEstudiante());
        EstudianteResponseDTO estudianteResponseDTO = null;

        if (estudianteDTO != null) {
            estudianteResponseDTO = new EstudianteResponseDTO();
            estudianteResponseDTO.setId(estudianteDTO.getId());
            estudianteResponseDTO.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
            estudianteResponseDTO.setTieneCertificado(estudianteDTO.getCertificadoNacimiento() != null &&
                    estudianteDTO.getCertificadoNacimiento().length > 0);
            estudianteResponseDTO.setUsuarioId(estudianteDTO.getUsuarioId());
            estudianteResponseDTO.setUsuario(estudianteDTO.getUsuario());
        }

        return InscripcionGradoResponseDTO.builder()
                .id(inscripcion.getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .gestion(inscripcion.getGestion())
                .estudiante(estudianteResponseDTO)
                .grado(gradoMapper.toDTO(inscripcion.getGrado()))
                .build();
    }

    public List<InscripcionGradoResponseDTO> toResponseDTOList(List<Inscripcion_grados> inscripciones) {
        if (inscripciones == null) {
            return List.of();
        }

        return inscripciones.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Inscripcion_grados toEntity(InscripcionGradoRequestDTO requestDTO, Estudiantes estudiante, Grados grado) {
        if (requestDTO == null) {
            return null;
        }

        Inscripcion_grados inscripcion = new Inscripcion_grados();
        inscripcion.setFechaInscripcion(requestDTO.getFechaInscripcion() != null ?
                requestDTO.getFechaInscripcion() : LocalDateTime.now());
        inscripcion.setGestion(requestDTO.getGestion());
        inscripcion.setEstudiante(estudiante);
        inscripcion.setGrado(grado);

        return inscripcion;
    }

    public void updateEntity(Inscripcion_grados inscripcion, InscripcionGradoRequestDTO requestDTO,
                             Estudiantes estudiante, Grados grado) {

        if (requestDTO.getFechaInscripcion() != null) {
            inscripcion.setFechaInscripcion(requestDTO.getFechaInscripcion());
        }

        if (requestDTO.getGestion() != null) {
            inscripcion.setGestion(requestDTO.getGestion());
        }

        if (estudiante != null) {
            inscripcion.setEstudiante(estudiante);
        }

        if (grado != null) {
            inscripcion.setGrado(grado);
        }
    }
}