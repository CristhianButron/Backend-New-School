package com.newschool.New.School.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "docente")
@NamedQueries({
        @NamedQuery(name = "Docentes.findAll", query = "SELECT d FROM Docentes d"),
        @NamedQuery(name = "Docentes.findByIdDocente", query = "SELECT d FROM Docentes d WHERE d.idDocente = :idDocente"),
        @NamedQuery(name = "Docentes.findByLicenciatura", query = "SELECT d FROM Docentes d WHERE d.licenciatura = :licenciatura")
})

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"titulo", "cursosList"}) // Excluir campos grandes del toString para evitar sobrecarga de memoria

public class Docentes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_docente")
    private Integer idDocente;

    @Basic(optional = false)
    @Column(name = "licenciatura", length = 250)
    private String licenciatura; // Cambiado de LocalDate a String para almacenar el nombre de la licenciatura

    @Lob
    @Column(name = "titulo")
    private byte[] titulo; // Añadido campo para almacenar el título del docente como un archivo binario

    @JoinColumn(name = "usuarios_id_usuarios", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuarioIdUsuario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docentesIdDocente", fetch = FetchType.LAZY)
    private List<Cursos> cursosList;
}