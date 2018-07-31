package com.api.pgc.core.APIRestPGC.models.mantenimiento;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "tbl_pais",
        indexes = {@Index(name = "idx_cod_pais", columnList = "COD_PAIS" )},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"COD_PAIS"})})
public class TblPais {
    //Propiedades de la tabla
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID_PAIS", columnDefinition = "serial")
    @ApiModelProperty(notes = "Identificador de la Tabla, se Autogenera")
    private long idPais;

    @Column(name = "COD_PAIS", nullable = false, length=5)
    @ApiModelProperty(notes = "Codigo Pais", required = true)
    private String codPais;

    @Column(name = "DESC_PAIS", nullable = false, length=150)
    @ApiModelProperty(notes = "Descripcion Pais", required = true)
    private String descPais;

    @Column(name = "INICIALES_PAIS", nullable = false, length=5)
    @ApiModelProperty(notes = "Iniciales Pais", required = true)
    private String inicialesPais;

    @Column(name = "CODIGO_POSTAL", nullable = false, length=5)
    @ApiModelProperty(notes = "Codigo Postal")
    private String codigoPostal;

    @Column(name = "COD_CONTINENTE", nullable = false, length=5)
    @ApiModelProperty(notes = "Cod Continente")
    private String codContinente;

    @Column(name = "LATITUD_PAIS", nullable = false, length=150)
    @ApiModelProperty(notes = "Latitud Pais")
    private String latitudPais;

    @Column(name = "LONGITUD_PAIS", nullable = false, length=150)
    @ApiModelProperty(notes = "Longitud Pais")
    private String longitudPais;

    public TblPais() {
    }

    public long getIdPais() {
        return idPais;
    }

    public void setIdPais(long idPais) {
        this.idPais = idPais;
    }

    public String getCodPais() {
        return codPais;
    }

    public void setCodPais(String codPais) {
        this.codPais = codPais;
    }

    public String getDescPais() {
        return descPais;
    }

    public void setDescPais(String descPais) {
        this.descPais = descPais;
    }

    public String getInicialesPais() {
        return inicialesPais;
    }

    public void setInicialesPais(String inicialesPais) {
        this.inicialesPais = inicialesPais;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCodContinente() {
        return codContinente;
    }

    public void setCodContinente(String codContinente) {
        this.codContinente = codContinente;
    }

    public String getLatitudPais() {
        return latitudPais;
    }

    public void setLatitudPais(String latitudPais) {
        this.latitudPais = latitudPais;
    }

    public String getLongitudPais() {
        return longitudPais;
    }

    public void setLongitudPais(String longitudPais) {
        this.longitudPais = longitudPais;
    }
}
