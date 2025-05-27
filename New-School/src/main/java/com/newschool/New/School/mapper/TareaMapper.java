package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.tarea.TareaDTO;
import com.newschool.New.School.dto.tarea.TareaRequestDTO;
import com.newschool.New.School.dto.tarea.TareaResponseDTO;
import com.newschool.New.School.entity.Cursos;
import com.newschool.New.School.entity.Tareas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                .archivo(tarea.getArchivo())
                .fecha_entrega(tarea.getFecha_entrega())
                .puntaje_maximo(tarea.getPuntaje_maximo())
                .cursoId(tarea.getCurso().getId_curso())
                .build();
    }

    public List<TareaDTO> toDTOList(List<Tareas> tareas) {
        if (tareas == null) {
            return List.of();
        }
        return tareas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TareaResponseDTO toResponseDTO(Tareas tarea) {
        if (tarea == null) {
            return null;
        }

        return TareaResponseDTO.builder()
                .id(tarea.getId_tarea())
                .titulo(tarea.getTitulo())
                .descripcion(tarea.getDescripcion())
                .archivo(tarea.getArchivo())
                .fecha_entrega(tarea.getFecha_entrega())
                .puntaje_maximo(tarea.getPuntaje_maximo())
                .curso(cursoMapper.toDTO(tarea.getCurso()))
                .build();
    }

    public Tareas toEntity(TareaRequestDTO dto, Cursos curso) {
        if (dto == null) {
            return null;
        }

        Tareas tarea = new Tareas();
        tarea.setTitulo(dto.getTitulo());
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setArchivo(dto.getArchivo());
        tarea.setFecha_entrega(dto.getFecha_entrega());
        tarea.setPuntaje_maximo(dto.getPuntaje_maximo());
        tarea.setCurso(curso);

        return tarea;
    }

    public void updateEntity(Tareas tarea, TareaRequestDTO dto) {
        if (dto == null || tarea == null) {
            return;
        }

        if (dto.getTitulo() != null) {
            tarea.setTitulo(dto.getTitulo());
        }
        if (dto.getDescripcion() != null) {
            tarea.setDescripcion(dto.getDescripcion());
        }
        if (dto.getArchivo() != null) {
            tarea.setArchivo(dto.getArchivo());
        }
        if (dto.getFecha_entrega() != null) {
            tarea.setFecha_entrega(dto.getFecha_entrega());
        }
        if (dto.getPuntaje_maximo() > 0) {
            tarea.setPuntaje_maximo(dto.getPuntaje_maximo());
        }
    }
}