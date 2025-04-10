package com.newschool.New.School.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
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
@Table(name = "curso")
@NamedQueries({
        @NamedQuery(name = "Cursos.findAll", query = "SELECT c FROM Cursos c"),
        @NamedQuery(name = "Cursos.findByIdCurso", query = "SELECT c FROM Cursos c WHERE c.id_curso = :idCurso"),
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cursos implements Serializable {

    private static final long serialVersionUID = 1L;

    // Mantenemos la misma propiedad Java idCurso pero cambiamos el mapeo a la columna 'id'
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_curso")
    private Integer id_curso; // Cambia idCurso a id_cursos para que coincida con la columna

    // También agregamos un getter/setter para id que delegue a idCurso para compatibilidad
    public Integer getId() {
        return id_curso;
    }

    public void setId(Integer id) {
        this.id_curso = id;
    }

    @Column(name = "nombre")
    private String nombreCurso;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @JoinColumn(name = "Grados_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Grados grado;

    @JoinColumn(name = "Docente_id", referencedColumnName = "id_docente")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Docentes docente;

    // Las clases que hacen referencia a esta entidad usarán estos campos mappedBy
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Tareas> tareasList;

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Contenidos> contenidosList;
}