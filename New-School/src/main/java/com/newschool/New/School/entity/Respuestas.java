package com.newschool.New.School.entity;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDate;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "respuestas")
@NamedQueries({
    @NamedQuery(name = "Respuestas.findAll", query = "SELECT r FROM Respuestas r"),
    @NamedQuery(name = "Respuestas.findByIdRespuestas", query = "SELECT r FROM Respuestas r WHERE r.id_respuestas = :id_respuestas"),
    @NamedQuery(name = "Respuestas.findByRespuesta", query = "SELECT r FROM Respuestas r WHERE r.respuesta = :respuesta")})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Respuestas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_respuestas")
    private Integer id_respuestas;

    @Basic(optional = false)
    @Column(name = "respuesta")
    private String respuesta;

    @Basic(optional = true)
    @Column(name = "archivo")
    private Blob archivo;

    @Basic(optional = false)
    @Column(name = "puntaje")
    private int puntaje;

    @Basic(optional = false)
    @Column(name = "fecha_entrega") 
    private LocalDate fechaEntrega;

    @JoinColumn(name = "estudiantes_id_estudiantes", referencedColumnName = "id_estudiante")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudiantes estudiantesIdEstudiantes;

    @JoinColumn(name = "tareas_id_tareas", referencedColumnName = "id_tarea")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Tareas tareasIdTareas;
}
