package com.newschool.New.School.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.newschool.New.School.entity.Contenidos;
import com.newschool.New.School.entity.Cursos;
import com.newschool.New.School.dto.contenido.ContenidoDTO;
import com.newschool.New.School.dto.contenido.ContenidoRequestDTO;
import com.newschool.New.School.dto.contenido.ContenidoResponseDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContenidoMapper {
    
    private final CursoMapper cursoMapper;
    
    @Autowired
    public ContenidoMapper(CursoMapper cursoMapper) {
        this.cursoMapper = cursoMapper;
    }
    
    public ContenidoDTO toDTO(Contenidos contenido) {
        if (contenido == null) {
            return null;
        }
        
        return ContenidoDTO.builder()
                .id(contenido.getId_contenido())
                .titulo(contenido.getTitulo())
                .descripcion(contenido.getDescripcion())
                .tipo(contenido.getTipo())
                .url(contenido.getUrl())
                .fechaCreacion(contenido.getFecha_creacion())
                .cursoId(contenido.getCurso().getId_curso())
                .build();
    }
    
    public List<ContenidoDTO> toDTOList(List<Contenidos> contenidos) {
        if (contenidos == null) {
            return List.of();
        }
        return contenidos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public ContenidoResponseDTO toResponseDTO(Contenidos contenido) {
        if (contenido == null) {
            return null;
        }
        
        return ContenidoResponseDTO.builder()
                .id(contenido.getId_contenido())
                .titulo(contenido.getTitulo())
                .descripcion(contenido.getDescripcion())
                .tipo(contenido.getTipo())
                .url(contenido.getUrl())
                .fechaCreacion(contenido.getFecha_creacion())
                .curso(cursoMapper.toResponseDTO(contenido.getCurso()))
                .build();
    }
    
    public List<ContenidoResponseDTO> toResponseDTOList(List<Contenidos> contenidos) {
        if (contenidos == null) {
            return List.of();
        }
        return contenidos.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public Contenidos toEntity(ContenidoRequestDTO requestDTO, Cursos curso) {
        if (requestDTO == null) {
            return null;
        }
        
        Contenidos contenido = new Contenidos();
        contenido.setTitulo(requestDTO.getTitulo());
        contenido.setDescripcion(requestDTO.getDescripcion());
        contenido.setTipo(requestDTO.getTipo());
        contenido.setUrl(requestDTO.getUrl());
        contenido.setFecha_creacion(requestDTO.getFechaCreacion() != null ? 
                requestDTO.getFechaCreacion() : LocalDate.now());
        contenido.setCurso(curso);
        
        return contenido;
    }
    
    public void updateEntity(Contenidos contenido, ContenidoRequestDTO requestDTO, Cursos curso) {
        if (requestDTO.getTitulo() != null) {
            contenido.setTitulo(requestDTO.getTitulo());
        }
        if (requestDTO.getDescripcion() != null) {
            contenido.setDescripcion(requestDTO.getDescripcion());
        }
        if (requestDTO.getTipo() != null) {
            contenido.setTipo(requestDTO.getTipo());
        }
        if (requestDTO.getUrl() != null) {
            contenido.setUrl(requestDTO.getUrl());
        }
        if (requestDTO.getFechaCreacion() != null) {
            contenido.setFecha_creacion(requestDTO.getFechaCreacion());
        }
        if (curso != null) {
            contenido.setCurso(curso);
        }
    }
}