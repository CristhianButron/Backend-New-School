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

    private final TareaRepository tareaRepository;
    private final CursoRepository cursoRepository;
    private final TareaMapper tareaMapper;

    @Autowired
    public TareaService(TareaRepository tareaRepository, CursoRepository cursoRepository, TareaMapper tareaMapper) {
        this.tareaRepository = tareaRepository;
        this.cursoRepository = cursoRepository;
        this.tareaMapper = tareaMapper;
    }

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
        try {
            validateTareaRequestDTO(requestDTO);
            
            Cursos curso = cursoRepository.findById(requestDTO.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            
            // Verificar si ya existe una tarea con el mismo título en el mismo curso
            if (tareaRepository.findByTituloAndCursoIdCurso(requestDTO.getTitulo(), requestDTO.getCursoId()).isPresent()) {
                throw new RuntimeException("Ya existe una tarea con el mismo título en este curso");
            }
            
            Tareas tarea = tareaMapper.toEntity(requestDTO, curso);
            tarea = tareaRepository.save(tarea);
            return tareaMapper.toResponseDTO(tarea);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la tarea: " + e.getMessage());
        }
    }

    @Transactional
    public TareaResponseDTO actualizar(Integer id, TareaRequestDTO requestDTO) {
        validateTareaRequestDTO(requestDTO);

        Tareas tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // Verificar si el curso existe si se proporciona un nuevo cursoId
        if (requestDTO.getCursoId() != null) {
            Cursos curso = cursoRepository.findById(requestDTO.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            
            // Verificar si ya existe otra tarea con el mismo título en el nuevo curso
            var existingTarea = tareaRepository.findByTituloAndCursoIdCurso(requestDTO.getTitulo(), requestDTO.getCursoId());
            if (existingTarea.isPresent() && !existingTarea.get().getId_tarea().equals(id)) {
                throw new RuntimeException("Ya existe otra tarea con el mismo título en este curso");
            }
            
            tarea.setCurso(curso);
        }

        tareaMapper.updateEntity(tarea, requestDTO);
        tarea = tareaRepository.save(tarea);
        return tareaMapper.toResponseDTO(tarea);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!tareaRepository.existsById(id)) {
            throw new RuntimeException("Tarea no encontrada");
        }

        // Aquí podrías agregar verificaciones adicionales antes de eliminar
        // Por ejemplo, verificar si hay respuestas asociadas
        // if (respuestaRepository.existsByTareaId(id)) {
        //     throw new RuntimeException("No se puede eliminar la tarea porque tiene respuestas asociadas");
        // }

        tareaRepository.deleteById(id);
    }

    private void validateTareaRequestDTO(TareaRequestDTO requestDTO) {
        if (requestDTO.getTitulo() == null || requestDTO.getTitulo().trim().isEmpty()) {
            throw new RuntimeException("El título de la tarea no puede estar vacío");
        }

        if (requestDTO.getTitulo().length() < 3) {
            throw new RuntimeException("El título de la tarea debe tener al menos 3 caracteres");
        }

        if (requestDTO.getTitulo().length() > 100) {
            throw new RuntimeException("El título de la tarea no puede exceder los 100 caracteres");
        }

        if (requestDTO.getDescripcion() == null || requestDTO.getDescripcion().trim().isEmpty()) {
            throw new RuntimeException("La descripción de la tarea no puede estar vacía");
        }

        if (requestDTO.getFecha_entrega() == null) {
            throw new RuntimeException("La fecha de entrega es requerida");
        }

        if (requestDTO.getCursoId() == null) {
            throw new RuntimeException("El ID del curso es requerido");
        }

        if ( requestDTO.getPuntaje_maximo() <= 0) {
            throw new RuntimeException("El puntaje máximo debe ser mayor que 0");
        }
    }
}