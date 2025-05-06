package com.newschool.New.School.service;

import com.newschool.New.School.dto.curso.CursoDTO;
import com.newschool.New.School.dto.curso.CursoRequestDTO;
import com.newschool.New.School.dto.curso.CursoResponseDTO;
import com.newschool.New.School.entity.Cursos;
import com.newschool.New.School.entity.Docentes;
import com.newschool.New.School.entity.Grados;
import com.newschool.New.School.mapper.CursoMapper;
import com.newschool.New.School.repository.CursoRepository;
import com.newschool.New.School.repository.DocentesRepository;
import com.newschool.New.School.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DocentesRepository docenteRepository;

    @Autowired
    private GradoRepository gradoRepository;

    @Autowired
    private CursoMapper cursoMapper;

    @Transactional(readOnly = true)
    public List<CursoDTO> findAll() {
        return cursoRepository.findAll().stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CursoDTO findById(Integer id) {
        return cursoRepository.findById(id)
                .map(cursoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> findByDocenteId(Integer docenteId) {
        return cursoRepository.findByDocenteIdDocente(docenteId).stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> findByGradoId(Integer gradoId) {
        return cursoRepository.findByGradoId(gradoId).stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CursoResponseDTO createCurso(CursoRequestDTO requestDTO) {
        // Verificar si el docente existe
        Docentes docente = docenteRepository.findById(requestDTO.getDocenteId())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        // Verificar si el grado existe
        Grados grado = gradoRepository.findById(requestDTO.getGradoId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        // Verificar si ya existe un curso con el mismo nombre
        if (cursoRepository.findByNombre(requestDTO.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe un curso con ese nombre");
        }

        // Crear y guardar el curso
        Cursos curso = new Cursos();
        curso.setNombre(requestDTO.getNombre());
        curso.setDescripcion(requestDTO.getDescripcion());
        curso.setDocente(docente);
        curso.setGrado(grado);

        curso = cursoRepository.save(curso);

        return cursoMapper.toResponseDTO(curso);
    }

    @Transactional
    public CursoResponseDTO updateCurso(Integer id, CursoRequestDTO requestDTO) {
        // Buscar el curso
        Cursos curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // Verificar si el docente existe
        if (requestDTO.getDocenteId() != null) {
            Docentes docente = docenteRepository.findById(requestDTO.getDocenteId())
                    .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
            curso.setDocente(docente);
        }

        // Verificar si el grado existe
        if (requestDTO.getGradoId() != null) {
            Grados grado = gradoRepository.findById(requestDTO.getGradoId())
                    .orElseThrow(() -> new RuntimeException("Grado no encontrado"));
            curso.setGrado(grado);
        }

        // Verificar si el nuevo nombre ya existe
        if (requestDTO.getNombre() != null && !requestDTO.getNombre().equals(curso.getNombre())) {
            if (cursoRepository.findByNombre(requestDTO.getNombre()).isPresent()) {
                throw new RuntimeException("Ya existe un curso con ese nombre");
            }
            curso.setNombre(requestDTO.getNombre());
        }

        if (requestDTO.getDescripcion() != null) {
            curso.setDescripcion(requestDTO.getDescripcion());
        }

        curso = cursoRepository.save(curso);

        return cursoMapper.toResponseDTO(curso);
    }

    @Transactional
    public void deleteCurso(Integer id) {
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("Curso no encontrado");
        }
        cursoRepository.deleteById(id);
    }
}