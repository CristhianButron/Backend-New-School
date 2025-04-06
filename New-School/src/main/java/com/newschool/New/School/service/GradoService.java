package com.newschool.New.School.service;

import com.newschool.New.School.dto.grado.GradoDTO;
import com.newschool.New.School.entity.Grados;
import com.newschool.New.School.mapper.GradoMapper;
import com.newschool.New.School.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GradoService {

    private final GradoRepository gradoRepository;
    private final GradoMapper gradoMapper;

    @Autowired
    public GradoService(GradoRepository gradoRepository, GradoMapper gradoMapper) {
        this.gradoRepository = gradoRepository;
        this.gradoMapper = gradoMapper;
    }

    @Transactional(readOnly = true)
    public List<GradoDTO> findAll() {
        return gradoMapper.toDTOList(gradoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public GradoDTO findById(Integer id) {
        return gradoRepository.findById(id)
                .map(gradoMapper::toDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public GradoDTO findByDescripcion(String descripcion) {
        return gradoRepository.findByDescripcion(descripcion)
                .map(gradoMapper::toDTO)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<GradoDTO> findByPrimariaSencundaria(Boolean esPrimaria) {
        return gradoMapper.toDTOList(gradoRepository.findByNivel(esPrimaria));
    }

    @Transactional
    public GradoDTO save(GradoDTO gradoDTO) {
        validateGradoDTO(gradoDTO);

        // Verificar si ya existe un grado con la misma descripción
        if (gradoDTO.getDescripcion() != null) {
            if (gradoRepository.findByDescripcion(gradoDTO.getDescripcion()).isPresent()) {
                return null;
            }
        }

        Grados grados = gradoMapper.toEntity(gradoDTO);
        Grados savedGrados = gradoRepository.save(grados);
        return gradoMapper.toDTO(savedGrados);
    }

    @Transactional
    public GradoDTO update(Integer id, GradoDTO gradoDTO) {
        validateGradoDTO(gradoDTO);

        var existingGradoOptional = gradoRepository.findById(id);
        if (existingGradoOptional.isEmpty()) {
            return null;
        }

        Grados existingGrado = existingGradoOptional.get();

        // Verificar si ya existe otro grado con la misma descripción (que no sea el actual)
        if (gradoDTO.getDescripcion() != null) {
            var existingWithSameDesc = gradoRepository.findByDescripcion(gradoDTO.getDescripcion());
            if (existingWithSameDesc.isPresent() && !existingWithSameDesc.get().getId().equals(id)) {
                return null;
            }
        }

        Grados updatedGrados = gradoMapper.updateEntity(existingGrado, gradoDTO);
        return gradoMapper.toDTO(gradoRepository.save(updatedGrados));
    }

    @Transactional
    public boolean deleteById(Integer id) {
        if (!gradoRepository.existsById(id)) {
            return false;
        }

        // Aquí podrías verificar si el grado tiene alumnos asociados antes de eliminarlo
        // Por ejemplo:
        // if (alumnoRepository.existsByGradoId(id)) {
        //     return false;
        // }

        gradoRepository.deleteById(id);
        return true;
    }

    private boolean validateGradoDTO(GradoDTO gradoDTO) {
        if (gradoDTO.getDescripcion() == null || gradoDTO.getDescripcion().trim().isEmpty()) {
            return false;
        }

        if (gradoDTO.getDescripcion().length() < 3) {
            return false;
        }

        if (gradoDTO.getDescripcion().length() > 100) {
            return false;
        }

        if (gradoDTO.getPrimariaSencundaria() == null) {
            return false;
        }

        return true;
    }
}