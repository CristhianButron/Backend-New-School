package com.newschool.New.School.entity;

import java.io.Serializable;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tarea")
@NamedQueries({
    @NamedQuery(name = "Tareas.findAll", query = "SELECT t FROM Tareas t"),
    @NamedQuery(name = "Tareas.findByIdTarea", query = "SELECT t FROM Tareas t WHERE t.id_tarea = :id_tarea"),
    @NamedQuery(name = "Tareas.findByTitulo", query = "SELECT t FROM Tareas t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "Tareas.findByDescripcion", query = "SELECT t FROM Tareas t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Tareas.findByArchivo", query = "SELECT t FROM Tareas t WHERE t.archivo = :archivo"),
    @NamedQuery(name = "Tareas.findByFechaEntrega", query = "SELECT t FROM Tareas t WHERE t.fecha_entrega = :fecha_entrega"),
    @NamedQuery(name = "Tareas.findByPuntajeMaximo", query = "SELECT t FROM Tareas t WHERE t.puntaje_maximo = :puntaje_maximo")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Tareas implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_tarea")
    private Integer id_tarea;

    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;

    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "archivo")
    private String archivo;

    @Basic(optional = false)
    @Column(name = "fecha_entrega")
    private String fecha_entrega;

    @Basic(optional = false)
    @Column(name = "puntaje_maximo")
    private int puntaje_maximo;

    @OneToMany(mappedBy = "tareasIdTareas", fetch = FetchType.LAZY)
    private List<Respuestas> RespuestasList;

    // @JoinColumn(name = "cursos_id_cursos", referencedColumnName = "id_curso")
    // @ManyToOne(optional = false, fetch = FetchType.LAZY)
    // private Curso cursoIdCurso;
    
}
