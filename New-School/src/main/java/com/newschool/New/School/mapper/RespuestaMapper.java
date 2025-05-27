package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.respuesta.RespuestaDTO;
import com.newschool.New.School.dto.respuesta.RespuestaResponseDTO;
import com.newschool.New.School.entity.Respuestas;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Tareas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RespuestaMapper {

    @Autowired
    private EstudianteMapper estudianteMapper;

    @Autowired
    private TareaMapper tareaMapper;

    /**
     * Convierte una entidad Respuestas a un DTO
     */
    public RespuestaDTO toDTO(Respuestas respuesta) {
        if (respuesta == null) {
            return null;
        }

        RespuestaDTO dto = new RespuestaDTO();
        dto.setId(respuesta.getId_respuestas());
        dto.setRespuesta(respuesta.getRespuesta());
        dto.setArchivo(respuesta.getArchivo());
        dto.setPuntaje(respuesta.getPuntaje());
        dto.setFechaEntrega(respuesta.getFechaEntrega());

        if (respuesta.getEstudiantesIdEstudiantes() != null) {
            dto.setEstudianteId(respuesta.getEstudiantesIdEstudiantes().getIdEstudiante());
        }

        if (respuesta.getTareasIdTareas() != null) {
            dto.setTareaId(respuesta.getTareasIdTareas().getId_tarea());
        }

        return dto;
    }

    /**
     * Convierte un DTO a una entidad Respuestas
     */
    public Respuestas toEntity(RespuestaDTO dto, Estudiantes estudiante, Tareas tarea) {
        if (dto == null) {
            return null;
        }

        Respuestas respuesta = new Respuestas();
        if (dto.getId() != null) {
            respuesta.setId_respuestas(dto.getId());
        }
        respuesta.setRespuesta(dto.getRespuesta());
        respuesta.setArchivo(dto.getArchivo());
        respuesta.setPuntaje(dto.getPuntaje());
        respuesta.setFechaEntrega(dto.getFechaEntrega());
        respuesta.setEstudiantesIdEstudiantes(estudiante);
        respuesta.setTareasIdTareas(tarea);

        return respuesta;
    }

    /**
     * Convierte una entidad Respuestas a un ResponseDTO
     */
    public RespuestaResponseDTO toResponseDTO(Respuestas respuesta) {
        if (respuesta == null) {
            return null;
        }

        return RespuestaResponseDTO.builder()
                .id(respuesta.getId_respuestas())
                .respuesta(respuesta.getRespuesta())
                .archivo(respuesta.getArchivo())
                .puntaje(respuesta.getPuntaje())
                .fechaEntrega(respuesta.getFechaEntrega())
                .estudiante(estudianteMapper.toDTO(respuesta.getEstudiantesIdEstudiantes()))
                .tarea(tareaMapper.toDTO(respuesta.getTareasIdTareas()))
                .tieneArchivo(respuesta.getArchivo() != null && !respuesta.getArchivo().isEmpty())
                .build();
    }

    /**
     * Actualiza una entidad existente con datos de un DTO
     */
    public void updateEntityFromDTO(RespuestaDTO dto, Respuestas respuesta) {
        if (dto == null || respuesta == null) {
            return;
        }

        if (dto.getRespuesta() != null) {
            respuesta.setRespuesta(dto.getRespuesta());
        }
        if (dto.getArchivo() != null) {
            respuesta.setArchivo(dto.getArchivo());
        }
        respuesta.setPuntaje(dto.getPuntaje());
        if (dto.getFechaEntrega() != null) {
            respuesta.setFechaEntrega(dto.getFechaEntrega());
        }
    }

    /**
     * Convierte una lista de Respuestas a una lista de ResponseDTO
     */
    public List<RespuestaResponseDTO> toResponseDTOList(List<Respuestas> respuestas) {
        if (respuestas == null) {
            return List.of();
        }
        return respuestas.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}