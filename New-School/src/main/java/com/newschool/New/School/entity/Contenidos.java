package com.newschool.New.School.entity;

import java.io.Serializable;
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
@Table(name = "contenidos")
@NamedQueries({ 
    @NamedQuery(name = "Contenidos.findAll", query = "SELECT c FROM Contenidos c"),
    @NamedQuery(name = "Contenidos.findByIdContenido", query = "SELECT c FROM Contenidos c WHERE c.id_contenido = :id_contenido"),
    @NamedQuery(name = "Contenidos.findByTitulo", query = "SELECT c FROM Contenidos c WHERE c.titulo = :titulo"),
    @NamedQuery(name = "Contenidos.findByDescripcion", query = "SELECT c FROM Contenidos c WHERE c.descripcion = :descripcion"),
    @NamedQuery(name = "Contenidos.findByTipo", query = "SELECT c FROM Contenidos c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "Contenidos.findByUrl", query = "SELECT c FROM Contenidos c WHERE c.url = :url")
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Contenidos implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contenido")
    private Integer id_contenido;

    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;

    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;

    @Basic(optional = false)
    @Column(name = "url")
    private String url;

    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    private LocalDate fecha_creacion;

    @JoinColumn(name = "cursos_id_cursos", referencedColumnName = "id_cursos")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cursos cursosIdCurso; 
    
}
