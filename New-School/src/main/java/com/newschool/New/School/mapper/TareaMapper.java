package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.tarea.TareaDTO;
import com.newschool.New.School.dto.tarea.TareaResponseDTO;
import com.newschool.New.School.entity.Cursos;
import com.newschool.New.School.entity.Tareas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TareaMapper {

    @Autowired
    private CursoMapper cursoMapper;

    public TareaDTO toDTO(Tareas tarea) {
        if (tarea == null) {
            return null;
        }

        return TareaDTO.builder()
                .id(tarea.getId_tarea())
                .titulo(tarea.getTitulo())
                .descripcion(tarea.getDescripcion())
                .fechaCreacion(tarea.getFechaCreacion())
                .fechaEntrega(tarea.getFechaEntrega())
                .cursoId(tarea.getCurso().getId_curso())
                .build();
    }

    public List<TareaDTO> toDTOList(List<Tareas> tareas) {
        return tareas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TareaResponseDTO toResponseDTO(Tareas tarea) {
        if (tarea == null) {
            return null;
        }

        return TareaResponseDTO.builder()
                .id(tarea.getId())
                .titulo(tarea.getTitulo())
                .descripcion(tarea.getDescripcion())
                .fechaCreacion(tarea.getFechaCreacion())
                .fechaEntrega(tarea.getFechaEntrega())
                .curso(cursoMapper.toDTO(tarea.getCurso()))
                .build();
    }

    public Tareas toEntity(TareaDTO dto, Cursos curso) {
        if (dto == null) {
            return null;
        }

        Tareas tarea = new Tareas();
        tarea.setId(dto.getId());
        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setFechaCreacion(LocalDateTime.now());
        tarea.setFechaEntrega(dto.getFechaEntrega());
        tarea.setCurso(curso);

        return tarea;
    }
}