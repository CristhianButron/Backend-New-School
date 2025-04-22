package com.newschool.New.School.service;

import com.newschool.New.School.dto.padre.PadreDTO;
import com.newschool.New.School.dto.padre.PadreRequestDTO;
import com.newschool.New.School.dto.padre.PadreResponseDTO;
import com.newschool.New.School.entity.Padres;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.mapper.PadreMapper;
import com.newschool.New.School.mapper.UsuarioMapper;
import com.newschool.New.School.repository.PadresRepository;
import com.newschool.New.School.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PadreServiceTest {

    @Mock
    private PadresRepository padresRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private PadreMapper padreMapper = new PadreMapper();
    private UsuarioMapper usuarioMapper = new UsuarioMapper();

    private PadreService padreService;

    private Usuario usuario;
    private Padres padre;
    private PadreRequestDTO padreRequestDTO;

    @BeforeEach
    void setUp() {
        // Configurar el UsuarioMapper en el PadreMapper
        ReflectionTestUtils.setField(padreMapper, "usuarioMapper", usuarioMapper);

        // Configurar el servicio manualmente
        padreService = new PadreService();
        ReflectionTestUtils.setField(padreService, "padresRepository", padresRepository);
        ReflectionTestUtils.setField(padreService, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(padreService, "padreMapper", padreMapper);

        // Configurar datos de prueba
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNombre("Carlos");
        usuario.setApellido("Pérez");
        usuario.setEmail("carlos.perez@example.com");
        usuario.setPassword("password123");
        usuario.setRol("PADRE");

        padre = new Padres();
        padre.setIdPadre(1);
        padre.setParentesco("Padre");
        padre.setUsuarioIdUsuario(usuario);

        padreRequestDTO = PadreRequestDTO.builder()
                .parentesco("Padre")
                .usuarioId(1)
                .build();
    }

    @Test
    void findAll() {
        // Arrange
        List<Padres> padresList = Arrays.asList(padre);
        when(padresRepository.findAll()).thenReturn(padresList);

        // Act
        List<PadreDTO> result = padreService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Padre", result.get(0).getParentesco());
        assertEquals(1, result.get(0).getUsuarioId());
        assertNotNull(result.get(0).getUsuario());
        assertEquals("Carlos", result.get(0).getUsuario().getNombre());
        verify(padresRepository).findAll();
    }

    @Test
    void findById() {
        // Arrange - Caso cuando existe
        when(padresRepository.findById(1)).thenReturn(Optional.of(padre));

        // Act
        PadreDTO result = padreService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Padre", result.getParentesco());
        assertEquals(1, result.getUsuarioId());
        assertNotNull(result.getUsuario());
        assertEquals("Carlos", result.getUsuario().getNombre());
        verify(padresRepository).findById(1);

        // Arrange - Caso cuando no existe
        when(padresRepository.findById(99)).thenReturn(Optional.empty());

        // Act
        PadreDTO notFoundResult = padreService.findById(99);

        // Assert
        assertNull(notFoundResult);
        verify(padresRepository).findById(99);
    }

    @Test
    void createPadre_Success() {
        // Arrange
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
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
        assertEquals(1, result.getUsuarioId());
        assertNotNull(result.getUsuario());
        assertEquals("Carlos", result.getUsuario().getNombre());
        verify(usuarioRepository).findById(1);
        verify(padresRepository).save(any(Padres.class));
    }

    @Test
    void createPadre_UserNotFound() {
        // Arrange
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            padreService.createPadre(
                    PadreRequestDTO.builder()
                            .parentesco("Padre")
                            .usuarioId(99)
                            .build()
            );
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository).findById(99);
        verify(padresRepository, never()).save(any(Padres.class));
    }

    @Test
    void deletePadre_Success() {
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
    void deletePadre_NotFound() {
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