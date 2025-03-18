/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.newschool.New.School.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "padre")
@NamedQueries({
    @NamedQuery(name = "Padres.findAll", query = "SELECT p FROM Padres p"),
    @NamedQuery(name = "Padres.findByIdpadre", query = "SELECT p FROM Padres p WHERE p.idpadre = :idpadre"),
    @NamedQuery(name = "Padres.findByParentesco", query = "SELECT p FROM Padres p WHERE p.parentesco = :parentesco")})
    
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Padres implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_padre")
    private Integer idpadre;

    @Basic(optional = false)
    @Column(name = "parentesco")
    private String parentesco;

    @JoinColumn(name = "usuarios_id_usuarios", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuarioIdUsuario;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "padresIdpadres", fetch = FetchType.LAZY)
    private List<PadresEstudiantes> padresEstudiantesList;
}
