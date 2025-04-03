package com.newschool.New.School.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Métodos específicos para Grado
    public static ResourceNotFoundException gradoNotFound(Integer id) {
        return new ResourceNotFoundException("No se encontró el grado con id: " + id);
    }

    public static ResourceNotFoundException gradoNotFoundByDescripcion(String descripcion) {
        return new ResourceNotFoundException("No se encontró el grado con descripción: " + descripcion);
    }
}