package com.newschool.New.School.service;

import com.newschool.New.School.dto.contenido.ContenidoDTO;
import com.newschool.New.School.dto.contenido.ContenidoRequestDTO;
import com.newschool.New.School.dto.contenido.ContenidoResponseDTO;
import com.newschool.New.School.entity.Contenidos;
import com.newschool.New.School.entity.Cursos;
import com.newschool.New.School.mapper.ContenidoMapper;
import com.newschool.New.School.repository.ContenidoRepository;
import com.newschool.New.School.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContenidoService {

    private final ContenidoRepository contenidoRepository;
    private final CursoRepository cursoRepository;
    private final ContenidoMapper contenidoMapper;

    @Autowired
    public ContenidoService(ContenidoRepository contenidoRepository, 
                          CursoRepository cursoRepository,
                          ContenidoMapper contenidoMapper) {
        this.contenidoRepository = contenidoRepository;
        this.cursoRepository = cursoRepository;
        this.contenidoMapper = contenidoMapper;
    }

    @Transactional
    public ContenidoResponseDTO crear(ContenidoRequestDTO requestDTO) {
        Cursos curso = cursoRepository.findById(requestDTO.getCursoId())
            .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + requestDTO.getCursoId()));
        
        Contenidos contenido = contenidoMapper.toEntity(requestDTO, curso);
        contenido = contenidoRepository.save(contenido);
        return contenidoMapper.toResponseDTO(contenido);
    }

    public Optional<ContenidoDTO> obtenerPorId(Integer id) {
        return contenidoRepository.findById(id)
                .map(contenidoMapper::toDTO);
    }

  

    public List<ContenidoDTO> obtenerPorTipo(String tipo) {
        return contenidoMapper.toDTOList(contenidoRepository.findByTipo(tipo));
    }

    @Transactional
    public ContenidoResponseDTO actualizar(Integer id, ContenidoRequestDTO requestDTO) {
        Contenidos contenido = contenidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contenido no encontrado con ID: " + id));

        Cursos curso = null;
        if (requestDTO.getCursoId() != null) {
            curso = cursoRepository.findById(requestDTO.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + requestDTO.getCursoId()));
        }

        contenidoMapper.updateEntity(contenido, requestDTO, curso);
        contenido = contenidoRepository.save(contenido);
        return contenidoMapper.toResponseDTO(contenido);
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!contenidoRepository.existsById(id)) {
            throw new RuntimeException("Contenido no encontrado con ID: " + id);
        }
        contenidoRepository.deleteById(id);
    }

    public List<ContenidoDTO> obtenerTodos() {
        return contenidoMapper.toDTOList(contenidoRepository.findAll());
    }
}