/*
 * Copyright (c) 2019.  Nahum Martinez
 */

package com.api.pgc.core.APIRestPGC.models.actividades.organizaciones;

import com.api.pgc.core.APIRestPGC.models.actividades.TblActividad;
import com.api.pgc.core.APIRestPGC.models.organizaciones.TblOrganizacion;
import com.api.pgc.core.APIRestPGC.models.programas.TblProgramaPlanNacion;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_actividades_socio_desarrollo",
        indexes = {@Index(name = "idx_codigo_actividad_org_socio_desarrollo", columnList = "CODIGO_ACTIVIDAD")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"CODIGO_ACTIVIDAD"})})
public class TblActividadOrganizacionSocioDesarrollo {
    // Propiedades de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ACTIVIDAD_SOCIO_DESARROLLO", columnDefinition = "serial")
    @ApiModelProperty(notes = "Identificador de la Tabla, se Autogenera")
    private long idActividadSocioDesarrollo;

    @Column(name = "CODIGO_ACTIVIDAD", nullable = false, length = 50)
    @ApiModelProperty(notes = "Codigo de la Actividad", required = true)
    private String codigoActividad;

    // Mapeo de la Relacion de la Tabla de Organizaciones con Actividades
    // Muchos Organizacion = 1 Actividad
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "ID_ORGANIZACION", referencedColumnName = "ID_ORGANIZACION")
    @ApiModelProperty(notes = "Entidad Organizacion, se envia desde un Json (\"idOrganizacion\": { \"idOrganizacion\": \"valor\" })", required = true)
    private TblOrganizacion idOrganizacion;

    // Mapeo de la Relacion de la Tabla de Actividades con Organizaciones
    // Muchos Sector Ocde = 1 Actividad
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "ID_ACTIVIDAD", referencedColumnName = "ID_ACTIVIDAD")
    @ApiModelProperty(notes = "Entidad de la Proyectos, se envia desde un Json (\"idActividad\": { \"idActividad\": \"valor\" })", required = true)
    private TblActividad idActividad;

    // Auditoria
    @Column(name = "ACTIVO")
    @ApiModelProperty(notes = "Activo", readOnly = true)
    private boolean activo = true;

    @Column(name = "FECHA_CREACION", columnDefinition = "date DEFAULT '2999-12-31'")
    @Temporal(TemporalType.DATE)
    @ApiModelProperty(notes = "Fecha de Creacion", readOnly = true)
    private Date fechaCreacion;

    @Column(name = "HORA_CREACION")
    @Temporal(TemporalType.TIME)
    @ApiModelProperty(notes = "Hora de Creacion de la Actividad, formato hh:mm:ss", readOnly = true)
    private Date horaCreacion = new Date();


    /**
     * Constructor vacio de la Clase, solo para Jpa
     */
    public TblActividadOrganizacionSocioDesarrollo() {
        // Este lo usa Jpa para realizar los Mapping
    }


    /**
     * Metodos Getters y Setter
     */
    public long getIdActividadSocioDesarrollo() {
        return idActividadSocioDesarrollo;
    }

    public void setIdActividadSocioDesarrollo(long idActividadSocioDesarrollo) {
        this.idActividadSocioDesarrollo = idActividadSocioDesarrollo;
    }

    public String getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(String codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public TblOrganizacion getIdOrganizacion() {
        return idOrganizacion;
    }

    public void setIdOrganizacion(TblOrganizacion idOrganizacion) {
        this.idOrganizacion = idOrganizacion;
    }

    public TblActividad getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(TblActividad idActividad) {
        this.idActividad = idActividad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getHoraCreacion() {
        return horaCreacion;
    }

    public void setHoraCreacion(Date horaCreacion) {
        this.horaCreacion = horaCreacion;
    }
}
