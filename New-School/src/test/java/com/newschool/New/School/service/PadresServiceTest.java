package com.newschool.New.School.service;

import com.newschool.New.School.dto.padre.PadreDTO;
import com.newschool.New.School.dto.padre.PadreRequestDTO;
import com.newschool.New.School.dto.padre.PadreResponseDTO;
import com.newschool.New.School.entity.Padres;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.PadreMapper;
import com.newschool.New.School.repository.PadresRepository;
import com.newschool.New.School.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PadresServiceTest {

    @Mock
    private PadresRepository padresRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private PadreMapper padreMapper = new PadreMapper();

    @InjectMocks
    private PadreService padreService;

    private Usuario usuario;
    private Padres padre;
    private PadreRequestDTO padreRequestDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setRol("PADRE");

        padre = new Padres();
        padre.setIdPadre(1);
        padre.setParentesco("Padre");
        padre.setUsuarioIdUsuario(usuario);

        padreRequestDTO = PadreRequestDTO.builder()
                .usuarioId(1)
                .parentesco("Padre")
                .build();
    }

    @Test
    void findAll() {
        // Arrange
        when(padresRepository.findAll()).thenReturn(Arrays.asList(padre));

        // Act
        List<PadreDTO> result = padreService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Padre", result.get(0).getParentesco());
        verify(padresRepository).findAll();
    }

    @Test
    void findById_WhenPadreExists() {
        // Arrange
        when(padresRepository.findById(1)).thenReturn(Optional.of(padre));

        // Act
        PadreDTO result = padreService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Padre", result.getParentesco());
        verify(padresRepository).findById(1);
    }

    @Test
    void findById_WhenPadreDoesNotExist() {
        // Arrange
        when(padresRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        PadreDTO result = padreService.findById(99);

        // Assert
        assertNull(result);
        verify(padresRepository).findById(99);
    }

    @Test
    void findByUsuarioId_WhenPadreExists() {
        // Arrange
        when(padresRepository.findByUsuarioId(1)).thenReturn(Optional.of(padre));

        // Act
        PadreDTO result = padreService.findByUsuarioId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Padre", result.getParentesco());
        verify(padresRepository).findByUsuarioId(1);
    }

    @Test
    void findByUsuarioId_WhenPadreDoesNotExist() {
        // Arrange
        when(padresRepository.findByUsuarioId(99)).thenReturn(Optional.empty());

        // Act
        PadreDTO result = padreService.findByUsuarioId(99);

        // Assert
        assertNull(result);
        verify(padresRepository).findByUsuarioId(99);
    }

    @Test
    void createPadre_WhenUsuarioExistsAndIsPadre() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(padresRepository.findByUsuarioId(1)).thenReturn(Optional.empty());
        when(padresRepository.save(any(Padres.class))).thenAnswer(invocation -> {
            Padres savedPadre = invocation.getArgument(0);
            savedPadre.setIdPadre(1);
            return savedPadre;
        });

        // Act
        PadreResponseDTO result = padreService.createPadre(padreRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Padre", result.getParentesco());
        verify(usuarioRepository).findById(1);
        verify(padresRepository).findByUsuarioId(1);
        verify(padresRepository).save(any(Padres.class));
    }

    @Test
    void createPadre_WhenUsuarioDoesNotExist() {
        // Arrange
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            padreService.createPadre(PadreRequestDTO.builder().usuarioId(99).build());
        });
        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(99);
        verify(padresRepository, never()).save(any(Padres.class));
    }

    @Test
    void createPadre_WhenUsuarioIsNotPadre() {
        // Arrange
        usuario.setRol("ESTUDIANTE");
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            padreService.createPadre(padreRequestDTO);
        });
        assertEquals("El usuario no tiene el rol de PADRE", exception.getMessage());
        verify(usuarioRepository).findById(1);
        verify(padresRepository, never()).save(any(Padres.class));
    }

    @Test
    void updatePadre_WhenPadreExists() {
        // Arrange
        when(padresRepository.findById(1)).thenReturn(Optional.of(padre));
        when(padresRepository.save(any(Padres.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PadreRequestDTO updateRequest = PadreRequestDTO.builder()
                .parentesco("Tutor")
                .build();

        // Act
        PadreResponseDTO result = padreService.updatePadre(1, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Tutor", result.getParentesco());
        verify(padresRepository).findById(1);
        verify(padresRepository).save(any(Padres.class));
    }

    @Test
    void updatePadre_WhenPadreDoesNotExist() {
        // Arrange
        when(padresRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            padreService.updatePadre(99, padreRequestDTO);
        });
        assertEquals("Padre no encontrado", exception.getMessage());
        verify(padresRepository).findById(99);
        verify(padresRepository, never()).save(any(Padres.class));
    }

    @Test
    void deletePadre_WhenPadreExists() {
        // Arrange
        when(padresRepository.existsById(1)).thenReturn(true);
        doNothing().when(padresRepository).deleteById(1);

        // Act
        boolean result = padreService.deletePadre(1);

        // Assert
        assertTrue(result);
        verify(padresRepository).existsById(1);
        verify(padresRepository).deleteById(1);
    }

    @Test
    void deletePadre_WhenPadreDoesNotExist() {
        // Arrange
        when(padresRepository.existsById(99)).thenReturn(false);

        // Act
        boolean result = padreService.deletePadre(99);

        // Assert
        assertFalse(result);
        verify(padresRepository).existsById(99);
        verify(padresRepository, never()).deleteById(anyInt());
    }
}
