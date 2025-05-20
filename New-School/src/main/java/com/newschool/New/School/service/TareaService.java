package com.newschool.New.School.service;

import com.newschool.New.School.dto.tarea.TareaDTO;
import com.newschool.New.School.dto.tarea.TareaRequestDTO;
import com.newschool.New.School.dto.tarea.TareaResponseDTO;
import com.newschool.New.School.entity.Cursos;
import com.newschool.New.School.entity.Tareas;
import com.newschool.New.School.mapper.TareaMapper;
import com.newschool.New.School.repository.CursoRepository;
import com.newschool.New.School.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TareaMapper tareaMapper;

    @Transactional(readOnly = true)
    public List<TareaDTO> findAll() {
        return tareaMapper.toDTOList(tareaRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TareaDTO findById(Integer id) {
        return tareaRepository.findById(id)
                .map(tareaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<TareaDTO> findByCursoId(Integer cursoId) {
        return tareaMapper.toDTOList(tareaRepository.findByCursoId_curso(cursoId));
    }

    @Transactional
    public TareaResponseDTO crear(TareaRequestDTO requestDTO) {
        Cursos curso = cursoRepository.findById(requestDTO.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Tareas tarea = new Tareas();
        tarea.setTitulo(requestDTO.getTitulo());
        tarea.setDescripcion(requestDTO.getDescripcion());
        tarea.setFechaEntrega(requestDTO.getFechaEntrega());
        tarea.setCurso(curso);

        tarea = tareaRepository.save(tarea);
        return tareaMapper.toResponseDTO(tarea);
    }

    @Transactional
    public TareaResponseDTO actualizar(Integer id, TareaRequestDTO requestDTO) {
        Tareas tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        if (requestDTO.getCursoId() != null) {
            Cursos curso = cursoRepository.findById(requestDTO.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            tarea.setCurso(curso);
        }

        if (requestDTO.getTitulo() != null) {
            tarea.setTitulo(requestDTO.getTitulo());
        }

        if (requestDTO.getDescripcion() != null) {
            tarea.setDescripcion(requestDTO.getDescripcion());
        }

        if (requestDTO.getFechaEntrega() != null) {
            tarea.setFechaEntrega(requestDTO.getFechaEntrega());
        }

        tarea = tareaRepository.save(tarea);
        return tareaMapper.toResponseDTO(tarea);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!tareaRepository.existsById(id)) {
            throw new RuntimeException("Tarea no encontrada");
        }
        tareaRepository.deleteById(id);
    }
}