package com.newschool.New.School.service;

import com.newschool.New.School.dto.tarea.TareaDTO;
import com.newschool.New.School.dto.tarea.TareaRequestDTO;
import com.newschool.New.School.dto.tarea.TareaResponseDTO;
import com.newschool.New.School.entity.Cursos;
import com.newschool.New.School.entity.Tareas;
import com.newschool.New.School.mapper.TareaMapper;
import com.newschool.New.School.repository.CursoRepository;
import com.newschool.New.School.repository.TareaRepository;
import com.newschool.New.School.entity.Inscripcion_grados;
import com.newschool.New.School.entity.Estudiantes;
import com.newschool.New.School.entity.Usuario;
import com.newschool.New.School.repository.InscripcionGradoRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    @Autowired
    private InscripcionGradoRepository inscripcionGradoRepository;
    
    @Autowired
    private EmailService emailService;

    @Transactional(readOnly = true)
    public List<TareaDTO> findAll() {
        return tareaMapper.toDTOList(tareaRepository.findAll());
    }
    
    @Transactional(readOnly = true)
    public List<TareaResponseDTO> findAllResponse() {
        return tareaMapper.toResponseDTOList(tareaRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TareaDTO findById(Integer id) {
        return tareaRepository.findById(id)
                .map(tareaMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }
    
    @Transactional(readOnly = true)
    public TareaResponseDTO findByIdResponse(Integer id) {
        return tareaRepository.findById(id)
                .map(tareaMapper::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    @Transactional
    public TareaResponseDTO crear(TareaRequestDTO requestDTO) {
        try {
            validateTareaRequestDTO(requestDTO);
            
            Cursos curso = cursoRepository.findById(requestDTO.getCursoId())
                    .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
            
            Tareas tarea = tareaMapper.toEntity(requestDTO, curso);
            tarea = tareaRepository.save(tarea);
            
            // Enviar notificación por correo a los estudiantes del curso
            notificarNuevaTareaAEstudiantes(tarea);
            
            return tareaMapper.toResponseDTO(tarea);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la tarea: " + e.getMessage());
        }
    }
    
    /**
     * Envía notificaciones por correo a todos los estudiantes inscritos en el curso
     * cuando se crea una nueva tarea.
     */
    private void notificarNuevaTareaAEstudiantes(Tareas tarea) {
        try {
            // Obtener el grado asociado al curso
            com.newschool.New.School.entity.Grados grado = tarea.getCurso().getGrado();
            
            // Obtener todas las inscripciones para ese grado
            List<Inscripcion_grados> inscripciones = inscripcionGradoRepository.findByGradoId(grado.getId());
            
            if (inscripciones.isEmpty()) {
                System.out.println("No hay estudiantes inscritos en este grado.");
                return;
            }
            
            // Recopilar los correos electrónicos de los estudiantes
            Set<String> emailsSet = new HashSet<>(); // Usar Set para evitar duplicados
            
            for (Inscripcion_grados inscripcion : inscripciones) {
                Estudiantes estudiante = inscripcion.getEstudiante();
                if (estudiante != null && estudiante.getUsuarioIdUsuario() != null) {
                    Usuario usuario = estudiante.getUsuarioIdUsuario();
                    if (usuario != null && usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
                        emailsSet.add(usuario.getEmail());
                    }
                }
            }
            
            if (emailsSet.isEmpty()) {
                System.out.println("No se encontraron correos electrónicos válidos para los estudiantes.");
                return;
            }
            
            // Convertir el conjunto a un array de strings
            String[] emails = emailsSet.toArray(new String[0]);
            
            // Preparar el asunto y cuerpo del correo
            String subject = "Nueva tarea: " + tarea.getTitulo();
            String body = String.format(
                "Estimado estudiante,\n\n" +
                "Se ha creado una nueva tarea en el curso '%s':\n\n" +
                "Título: %s\n" +
                "Descripción: %s\n" +
                "Fecha de entrega: %s\n" +
                "Puntaje máximo: %d\n\n" +
                "Por favor, completa esta tarea antes de la fecha de entrega.\n\n" +
                "Saludos,\n" +
                "Equipo New School",
                tarea.getCurso().getNombre(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getFecha_entrega(),
                tarea.getPuntaje_maximo()
            );
            
            // Enviar el correo a todos los estudiantes
            emailService.sendEmailToMultipleRecipients(emails, subject, body);
            
            System.out.println("Notificación de nueva tarea enviada a " + emails.length + " estudiantes.");
            
        } catch (Exception e) {
            // Capturar la excepción pero no detener el flujo de la aplicación
            System.err.println("Error al enviar notificaciones por correo: " + e.getMessage());
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
            tarea.setCurso(curso);
        }

        tarea = tareaMapper.updateEntity(tarea, requestDTO);
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

        if (requestDTO.getPuntaje_maximo() <= 0) {
            throw new RuntimeException("El puntaje máximo debe ser mayor que 0");
        }
    }
}