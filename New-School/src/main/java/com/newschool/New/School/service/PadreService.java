package com.newschool.New.School.service;

import com.newschool.New.School.dto.padre.PadreDTO;
import com.newschool.New.School.dto.padre.PadreRequestDTO;
import com.newschool.New.School.dto.padre.PadreResponseDTO;
import com.newschool.New.School.entity.Padres;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.PadreMapper;
import com.newschool.New.School.repository.PadresRepository;
import com.newschool.New.School.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PadreService {

    @Autowired
    private PadresRepository padresRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PadreMapper padresMapper;

    /**
     * Obtiene todos los padres
     */
    public List<PadreDTO> findAll() {
        return padresRepository.findAll().stream()
                .map(padresMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un padre por su ID
     */
    public PadreDTO findById(Integer id) {
        return padresRepository.findById(id)
                .map(padresMapper::toDTO)
                .orElse(null);
    }

    /**
     * Busca un padre por el ID de usuario
     */
    public PadreDTO findByUsuarioId(Integer usuarioId) {
        return padresRepository.findByUsuarioId(usuarioId)
                .map(padresMapper::toDTO)
                .orElse(null);
    }

    /**
     * Crea un nuevo padre
     */
    @Transactional
    public PadreResponseDTO createPadre(PadreRequestDTO padreRequestDTO) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepository.findById(padreRequestDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el usuario tenga el rol de PADRE
        if (!"PADRE".equalsIgnoreCase(usuario.getRol())) {
            throw new RuntimeException("El usuario no tiene el rol de PADRE");
        }

        // Verificar que no exista un padre con ese ID de usuario
        if (padresRepository.findByUsuarioId(usuario.getIdUsuario()).isPresent()) {
            throw new RuntimeException("Ya existe un padre con ese ID de usuario");
        }

        // Crear y guardar el padre
        Padres padre = new Padres();
        padre.setUsuarioIdUsuario(usuario);
        padre.setParentesco(padreRequestDTO.getParentesco());

        padre = padresRepository.save(padre);

        // Crear la respuesta
        return PadreResponseDTO.builder()
                .id(padre.getIdPadre())
                .parentesco(padre.getParentesco())
                .usuarioId(usuario.getIdUsuario())
                .usuario(padresMapper.toUsuarioDTO(usuario))
                .build();
    }

    /**
     * Actualiza un padre existente
     */
    @Transactional
    public PadreResponseDTO updatePadre(Integer id, PadreRequestDTO padreRequestDTO) {
        // Buscar el padre
        Padres padre = padresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Padre no encontrado"));

        // Actualizar datos
        padre.setParentesco(padreRequestDTO.getParentesco());

        padre = padresRepository.save(padre);

        // Obtener el usuario asociado
        Usuario usuario = padre.getUsuarioIdUsuario();

        // Crear la respuesta
        return PadreResponseDTO.builder()
                .id(padre.getIdPadre())
                .parentesco(padre.getParentesco())
                .usuarioId(usuario.getIdUsuario())
                .usuario(padresMapper.toUsuarioDTO(usuario))
                .build();
    }

    /**
     * Elimina un padre
     */
    @Transactional
    public boolean deletePadre(Integer id) {
        if (padresRepository.existsById(id)) {
            padresRepository.deleteById(id);
            return true;
        }
        return false;
    }
}