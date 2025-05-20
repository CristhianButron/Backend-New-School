package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.respuesta.RespuestaDTO;
import com.newschool.New.School.dto.respuesta.RespuestaResponseDTO;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Respuestas;
import com.newschool.New.School.entity.Tareas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RespuestaMapper {

    @Autowired
    private TareaMapper tareaMapper;

    @Autowired
    private EstudianteMapper estudianteMapper;

    public RespuestaDTO toDTO(Respuestas respuesta) {
        if (respuesta == null) {
            return null;
        }

        return RespuestaDTO.builder()
                .id(respuesta.getId())
                .contenido(respuesta.getContenido())
                .fechaEnvio(respuesta.getFechaEnvio())
                .tareaId(respuesta.getTarea().getId())
                .estudianteId(respuesta.getEstudiante().getIdEstudiante())
                .archivo(respuesta.getArchivo())
                .build();
    }

    public List<RespuestaDTO> toDTOList(List<Respuestas> respuestas) {
        return respuestas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RespuestaResponseDTO toResponseDTO(Respuestas respuesta) {
        if (respuesta == null) {
            return null;
        }

        return RespuestaResponseDTO.builder()
                .id(respuesta.getId())
                .contenido(respuesta.getContenido())
                .fechaEnvio(respuesta.getFechaEnvio())
                .tarea(tareaMapper.toDTO(respuesta.getTarea()))
                .estudiante(estudianteMapper.toDTO(respuesta.getEstudiante()))
                .tieneArchivo(respuesta.getArchivo() != null && respuesta.getArchivo().length > 0)
                .build();
    }

    public Respuestas toEntity(RespuestaDTO dto, Tareas tarea, Estudiantes estudiante) {
        if (dto == null) {
            return null;
        }

        Respuestas respuesta = new Respuestas();
        respuesta.setId(dto.getId());
        respuesta.setContenido(dto.getContenido());
        respuesta.setFechaEnvio(LocalDateTime.now());
        respuesta.setTarea(tarea);
        respuesta.setEstudiante(estudiante);
        respuesta.setArchivo(dto.getArchivo());

        return respuesta;
    }
}