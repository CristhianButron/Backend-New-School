/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.newschool.New.School.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "administrativo")
@NamedQueries({
    @NamedQuery(name = "Administrativos.findAll", query = "SELECT a FROM Administrativos a"),
    @NamedQuery(name = "Administrativos.findByIdAdministrativo", query = "SELECT a FROM Administrativos a WHERE a.idAdministrativo = :idAdministrativo"),
    @NamedQuery(name = "Administrativos.findByCargo", query = "SELECT a FROM Administrativos a WHERE a.cargo = :cargo")})
    
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Administrativos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_administrativo")
    private Integer idAdministrativo;

    @Basic(optional = false)
    @Column(name = "Cargo")
    private LocalDate cargo;

    @JoinColumn(name = "usuarios_id_usuarios", referencedColumnName = "id_usuario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuarioIdUsuario;
}
