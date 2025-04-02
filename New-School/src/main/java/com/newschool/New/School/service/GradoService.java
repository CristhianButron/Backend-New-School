package com.newschool.New.School.service;

import com.newschool.New.School.dto.grado.GradoDTO;
import com.newschool.New.School.entity.Grados;
import com.newschool.New.School.mapper.GradoMapper;
import com.newschool.New.School.repository.GradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public Optional<GradoDTO> findById(Integer id) {
        return gradoRepository.findById(id)
                .map(gradoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Optional<GradoDTO> findByDescripcion(String descripcion) {
        return gradoRepository.findByDescripcion(descripcion)
                .map(gradoMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<GradoDTO> findByPrimariaSencundaria(Boolean esPrimaria) {
        return gradoMapper.toDTOList(gradoRepository.findByNivel(esPrimaria));
    }

    @Transactional
    public GradoDTO save(GradoDTO gradoDTO) {
        Grados grados = gradoMapper.toEntity(gradoDTO);
        Grados savedGrados = gradoRepository.save(grados);
        return gradoMapper.toDTO(savedGrados);
    }

    @Transactional
    public Optional<GradoDTO> update(Integer id, GradoDTO gradoDTO) {
        return gradoRepository.findById(id)
                .map(grados -> {
                    Grados updatedGrados = gradoMapper.updateEntity(grados, gradoDTO);
                    return gradoMapper.toDTO(gradoRepository.save(updatedGrados));
                });
    }

    @Transactional
    public boolean deleteById(Integer id) {
        if (!gradoRepository.existsById(id)) {
            return false;
        }
        gradoRepository.deleteById(id);
        return true;
    }
}