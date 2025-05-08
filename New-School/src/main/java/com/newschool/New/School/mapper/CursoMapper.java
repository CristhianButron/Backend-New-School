package com.newschool.New.School.mapper;

import com.newschool.New.School.dto.curso.CursoDTO;
import com.newschool.New.School.dto.curso.CursoResponseDTO;
import com.newschool.New.School.entity.Cursos;
import com.newschool.New.School.entity.Docentes;
import com.newschool.New.School.entity.Grados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CursoMapper {

    @Autowired
    private DocenteMapper docenteMapper;

    @Autowired
    private GradoMapper gradoMapper;

    public CursoDTO toDTO(Cursos curso) {
        if (curso == null) {
            return null;
        }

        return CursoDTO.builder()
                .id(curso.getId_curso())
                .nombre(curso.getNombre())
                .descripcion(curso.getDescripcion())
                .docenteId(curso.getDocente().getIdDocente())
                .gradoId(curso.getGrado().getId())
                .build();
    }

    public List<CursoDTO> toDTOList(List<Cursos> cursosList) {
        return cursosList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CursoResponseDTO toResponseDTO(Cursos curso) {
        if (curso == null) {
            return null;
        }

        return CursoResponseDTO.builder()
                .id(curso.getId_curso())
                .nombre(curso.getNombre())
                .descripcion(curso.getDescripcion())
                .docente(docenteMapper.toDTO(curso.getDocente()))
                .grado(gradoMapper.toDTO(curso.getGrado()))
                .build();
    }

    public Cursos toEntity(CursoDTO dto, Docentes docente, Grados grado) {
        if (dto == null) {
            return null;
        }

        Cursos curso = new Cursos();
        curso.setId_curso(dto.getId());
        curso.setNombre(dto.getNombre());
        curso.setDescripcion(dto.getDescripcion());
        curso.setDocente(docente);
        curso.setGrado(grado);
        
        return curso;
    }

    public Cursos updateEntity(Cursos curso, CursoDTO dto, Docentes docente, Grados grado) {
        curso.setNombre(dto.getNombre());
        curso.setDescripcion(dto.getDescripcion());
        curso.setDocente(docente);
        curso.setGrado(grado);
        return curso;
    }
}