package com.newschool.New.School.exception;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    // Métodos específicos para Grado
    public static BadRequestException gradoDuplicado(String descripcion) {
        return new BadRequestException("Ya existe un grado con la descripción: " + descripcion);
    }

    public static BadRequestException gradoConAlumnosActivos(Integer gradoId) {
        return new BadRequestException("No se puede eliminar el grado con id: " + gradoId +
                " porque tiene alumnos asignados");
    }
}
