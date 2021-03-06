/*
 * Copyright (c) 2019.  Nahum Martinez
 */

package com.api.pgc.core.APIRestPGC.resourses.actividades;


/*
 * Definicion de las Librerias a importar de la Clase
 */

import com.api.pgc.core.APIRestPGC.models.actividades.TblActividad;
import com.api.pgc.core.APIRestPGC.models.actividades.TblActividadUbicacion;
import com.api.pgc.core.APIRestPGC.models.seguridad.TblUsuarios;
import com.api.pgc.core.APIRestPGC.models.ubicacion_geografica.TblUbicacionImplementacion;
import com.api.pgc.core.APIRestPGC.repository.actividades.ActividadRepository;
import com.api.pgc.core.APIRestPGC.repository.actividades.ActividadUbicacionRepository;
import com.api.pgc.core.APIRestPGC.repository.seguridad.UsuariosRepository;
import com.api.pgc.core.APIRestPGC.repository.ubicacion_geografica.UbicacionImplementacionRepository;
import com.api.pgc.core.APIRestPGC.utilities.msgExceptions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import static com.api.pgc.core.APIRestPGC.utilities.configAPI.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = API_BASE_PATH)
@Api(value = "ActividadUbicacionAPI", description = "Operaciones sobre el Modulo de Proyectos Ubicaciones", tags = "Ubicacion de Proyectos")
public class ActividadUbicacionResourses {
    //Propiedades de la Clase
    String msgMethod = null;

    @Autowired
    ActividadUbicacionRepository _actividadUbicacionRepository;

    @Autowired
    ActividadRepository _actividadRepository;

    @Autowired
    UbicacionImplementacionRepository _ubicacionImplementacionRepository;

    @Autowired
    UsuariosRepository _usuariosRepository;

    /**
     * Metodo que despliega la Lista de todas las Ubicaciones de una Actividad de la BD
     *
     * @return Lista de Ubicaciones de una Actividad de la BD
     * @autor Nahum Martinez | NAM
     * @version 28/02/2019/v1.0
     */
    @ApiOperation(value = "Retorna el Listado de Todas las Ubicaciones registradas de los Proyectos de la BD", authorizations = {@Authorization(value = "Token-PGC")})
    @GetMapping(value = UBICACIONES_ACT_ENDPOINT, produces = "application/json; charset=UTF-8")
    public HashMap<String, Object> getAllUbicacionesActivities() throws Exception {
        //Ejecuta el try Cacth
        msgExceptions msgExeptions = new msgExceptions();

        msgMethod = "Listado de todas las Ubicaciones registradas en la BD";

        try {
            //Sobreescirbe el Metodo de Mensajes
            msgExeptions.map.put("data", _actividadUbicacionRepository.findAll());
            msgExeptions.map.put("totalRecords", _actividadUbicacionRepository.count());
            //Retorno del json
            return msgExeptions.msgJson(msgMethod, 200);
        } catch (Exception ex) {
            throw new RuntimeException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
        }
    }//FIN


    /**
     * Metodo que despliega las Ubicaciones asociadas a la Actividad de la BD
     *
     * @param idActividad Identificador del Proyecto a Buscar
     * @return Ubicaciones de la BD
     * @autor Nahum Martinez | NAM
     * @version 28/02/2019/v1.0
     */
    @ApiOperation(value = "Retorna las Ubicaciones, enviando a buscar por el Id del Proyecto de la BD", authorizations = {@Authorization(value = "Token-PGC")})
    @GetMapping(value = UBICACIONES_ACT_ENDPOINT_FIND_BY_ID_ACTIVIDAD, produces = "application/json; charset=UTF-8")
    public HashMap<String, Object> getUbicacionByIdActivity(@ApiParam(value = "Identificador de la Id Proyecto a Buscar", required = true)
                                                            @PathVariable("idActividad") long idActividad) throws Exception {
        //Ejecuta el try Cacth
        msgExceptions msgExeptions = new msgExceptions();

        try {
            // Busca la Actividad, desde el Reporsitorio con el Parametro del Json enviado ( "idActividad": {"idActividad": valor })
            TblActividad _tblActividad = _actividadRepository.findByIdActividad(idActividad);

            try {
                if (_actividadUbicacionRepository.getUbicacionByIdActividad(_tblActividad) == null) {
                    //Sobreescirbe el Metodo de Mensajes
                    msgMethod = "No se ha encontrado datos de ubicaciones, por el Proyecto consultado";

                    msgExeptions.map.put("error", "No data found");
                    msgExeptions.map.put("findRecords", false);

                    //Retorno del json
                    return msgExeptions.msgJson(msgMethod, 200);
                } else {
                    //Sobreescirbe el Metodo de Mensajes
                    msgMethod = "Detalle de las ubicaciones enocntradas por el Proyecto consultado";
                    msgExeptions.map.put("data", _actividadUbicacionRepository.getUbicacionByIdActividad(_tblActividad));
                    msgExeptions.map.put("totalRecords", _actividadUbicacionRepository.countByIdActividad(_tblActividad));
                    msgExeptions.map.put("findRecords", true);

                    //Retorno del json
                    return msgExeptions.msgJson(msgMethod, 200);
                }
            } catch (Exception ex) {
                //Sobreescirbe el Metodo de Mensajes
                msgMethod = "No se ha encomtrado informacion de las ubicaciones para el Proyecto consultado";
                throw new RuntimeException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
            }
        } catch (Exception ex) {
            //Sobreescirbe el Metodo de Mensajes
            msgMethod = "No se ha encontrado informacion del Proyecto consultado";
            throw new RuntimeException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
        }
    }//FIN


    /**
     * Metodo que Solicita un json con los datos de la Entidad Ubicaciones con Relacion
     * a Actividades
     *
     * @param _actividadUbicacionJson Obtiene desde el request los datos de la Ubicacion a ingresar
     * @return Mensaje de Confirmacion de Registro de Relacion de Ubicacion con Proyecto
     * @autor Nahum Martinez | NAM
     * @version 28/02/2019/v1.0
     */
    @ApiOperation(value = "Ingresa a la BD, la Información enviada por el Json Bean de la nueva Ubicacion del proyecto", authorizations = {@Authorization(value = "Token-PGC")})
    @PostMapping(value = UBICACIONES_ACT_ENDPOINT_NEW, produces = "application/json; charset=UTF-8")
    public HashMap<String, Object> addActividadUbicacion(@ApiParam(value = "Json de la nueva Ubicacion del Proyecto a Ingresar", required = true)
                                                         @RequestBody @Valid final TblActividadUbicacion _actividadUbicacionJson) throws Exception {
        // Ejecuta el try Cacth
        msgExceptions msgExeptions = new msgExceptions();

        // Fecha de Ingrso
        Date dateActual = new Date();

        try {
            // Busca la Actividad, desde el Reporsitorio con el Parametro del Json enviado ( "idActividad": {"idActividad": valor })
            TblActividad _tblActividad = _actividadRepository.findByIdActividad(_actividadUbicacionJson.getIdActividad().getIdActividad());

            try {
                // Busca la Ubicacion de Implementacion, desde el Reporsitorio con el Parametro del Json enviado ( "idUbicacionImpl": {"idUbicacionImplementacion": valor })
                TblUbicacionImplementacion _tblUbicacionImplementacion = _ubicacionImplementacionRepository.findByIdUbicacionImplementacion(_actividadUbicacionJson.getIdUbicacionImplementacion().getIdUbicacionImplementacion());

                try {
                    // Busca el Usuario Creador de la Actividad, desde el Reporsitorio con el Parametro del Json enviado ( "idUsuarioCreador": { "idUsuario": valor })
                    TblUsuarios _tblUsuarios = _usuariosRepository.findByIdUsuario(_actividadUbicacionJson.getIdUsuario().getIdUsuario());

                    // Busca el Proyecto con el Proposito de validar que no se meta otro Item mas,
                    // desde el Reporsitorio de Planificacion con el Parametro del Json enviado ( "idActividadIdInterna": _tblActividad )

                    if (_actividadUbicacionRepository.countByIdUbicacionImplementacionAndIdActividad(_tblUbicacionImplementacion, _tblActividad) > 0) {
                        msgMethod = "Ya Existe un registro con el Código de Ubicación para este Proyecto !!";

                        // Busca la Ubicacion Recien Ingresada
                        msgExeptions.map.put("data", _actividadUbicacionRepository.findByIdUbicacionImplementacionAndIdActividad(_tblUbicacionImplementacion, _tblActividad));
                        msgExeptions.map.put("findRecord", true);

                        // Ejecuta el Servicio de Actualizacion de la Ubicacion
                        // editActividadUbicacion(_actividadUbicacionJson.getIdActividadUbicacion(), _actividadUbicacionJson);

                        return msgExeptions.msgJson(msgMethod, 200);
                    } else {
                        // Seteo de las Fecha y Hora de Creacion
                        _actividadUbicacionJson.setFechaCreacion(dateActual);
                        _actividadUbicacionJson.setHoraCreacion(dateActual);
                        _actividadUbicacionJson.setPorcentajeUbicacion(_actividadUbicacionJson.getPorcentajeUbicacion());

                        // Seteamos la Actividad de la Id Interna y Organizacion
                        _actividadUbicacionJson.setIdActividad(_tblActividad);
                        _actividadUbicacionJson.setIdUbicacionImplementacion(_tblUbicacionImplementacion);
                        _actividadUbicacionJson.setIdUsuario(_tblUsuarios);

                        // Realizamos la Persistencia de los Datos
                        _actividadUbicacionRepository.save(_actividadUbicacionJson);
                        _actividadUbicacionRepository.flush();

                        // Busca la Ubicacion Recien Ingresada
                        msgExeptions.map.put("data", _actividadUbicacionRepository.findByIdUbicacionImplementacionAndIdActividad(_tblUbicacionImplementacion, _tblActividad));
                        msgExeptions.map.put("findRecord", false);

                        // Retorno de la Funcion
                        msgMethod = "La Ubicacion: " + _actividadUbicacionJson.getCodigoActividad() + " para este Proyecto, se ha Ingresado de forma satisfactoria!!";

                        //Retorno del json
                        return msgExeptions.msgJson(msgMethod, 200);
                    }
                } catch (Exception ex) {
                    msgMethod = "Ha Ocurrido un error al Intentar Grabar la Ubicacion del Proyecto, No existe el Usuario!!";
                    throw new SQLException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
                }
            } catch (Exception ex) {
                msgMethod = "Ha Ocurrido un error al Intentar Grabar la Ubicacion del Proyecto, No existe la Ubicacion seleccionada !!";
                throw new SQLException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
            }
        } catch (Exception ex) {
            msgMethod = "Ha Ocurrido un error al Intentar Grabar la Ubicacion del Proyecto,No existe el Proyecto";
            throw new RuntimeException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
        }
    } // FIN


    /**
     * Metodo que Solicita un json con los datos de la Entidad Ubicacion de Proyectos con Relacion
     * a Actividades
     *
     * @param idActividad     Identificador de la Actividad a Eliminar
     * @param idUbicacionImpl Identificador de la Ubicacion a Eliminar
     * @return Mensaje de Confirmacion de Eliminacion de la Ubicacion
     * @autor Nahum Martinez | NAM
     * @version 28/02/2019/v1.0
     */
    @ApiOperation(value = "Elimina de la BD, la Información enviada por el indentificador de Ubicacion", authorizations = {@Authorization(value = "Token-PGC")})
    @DeleteMapping(value = UBICACIONES_ACT_ENDPOINT_DELETE, produces = "application/json; charset=UTF-8")
    public HashMap<String, Object> deletedActividadUbicacion(@ApiParam(value = "Indentificar de la Ubicacion del Proyecto a Eliminar", required = true)
                                                             @PathVariable("idUbicacionImpl") long idUbicacionImpl,
                                                             @ApiParam(value = "Indentificar del Proyecto a Eliminar", required = true) @PathVariable("idActividad") long idActividad) throws Exception {
        // Ejecuta el try Cacth
        msgExceptions msgExeptions = new msgExceptions();

        try {
            // Busca la Actividad, desde el Reporsitorio con el Parametro del Json enviado ( "idActividad": {"idActividad": valor })
            TblActividad _tblActividad = _actividadRepository.findByIdActividad(idActividad);

            try {
                // Busca la Actividad, desde el Reporsitorio con el Parametro del Id enviado ( idUbicacionImpl )
                // TblActividadUbicacion _tblActividadUbicacion= _actividadUbicacionRepository.findByIdUbicacionImpl( _tblUbicacionImplementacion );
                // Busca la Actividad, desde el Reporsitorio con el Parametro del Id enviado ( idUbicacionImpl )
                TblUbicacionImplementacion _tblUbicacionImplementacion = _ubicacionImplementacionRepository.findByIdUbicacionImplementacion(idUbicacionImpl);

                try {
                    if (_actividadUbicacionRepository.countByIdUbicacionImplementacionAndIdActividad(_tblUbicacionImplementacion, _tblActividad) > 0) {
                        // Realizamos la Persistencia de los Datos
                        _actividadUbicacionRepository.deleletedUbicacionActividad(_tblUbicacionImplementacion, _tblActividad);
                        _actividadUbicacionRepository.flush();

                        // Retorno de la Funcion
                        msgMethod = "La Ubicacion para este Proyecto, se ha Eliminado de forma satisfactoria!!";

                        //Retorno del json
                        return msgExeptions.msgJson(msgMethod, 200);
                    } else {
                        msgMethod = "No Existe un registro de Ubicacion para este Proyecto !!";
                        throw new SQLException("Se ha producido una excepción con el mensaje : " + msgMethod);
                    }
                } catch (Exception ex) {
                    msgMethod = "Ha Ocurrido un error al Eliminar la Ubicacion del Proyecto !!";
                    throw new SQLException("Se ha producido una excepción con el mensaje: " + msgMethod, ex);
                }
            } catch (Exception ex) {
                msgMethod = "No Existe un registro de Proyecto, por favor verfica que lo has ingresado correctamente o que existe.";
                throw new RuntimeException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
            }
        } catch (Exception ex) {
            msgMethod = "No Existe un registro de Proyecto en Ubicaciones, por favor verfica que lo has ingresado correctamente o que existe.";
            throw new RuntimeException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
        }
    } // FIN


    /**
     * Metodo que Solicita un json con los datos de la Entidad Ubicaciones con Relacion
     * a Actividades
     *
     * @param _actividadUbicacionJson Obtiene desde el request los datos de la Ubicacion a ingresar
     * @param idActividadUbicacion    Identificardor de la tabla de Ubicaciones con Actiidades
     * @return Mensaje de Confirmacion de Registro de Relacion de Ubicacion con Proyecto
     * @autor Nahum Martinez | NAM
     * @version 28/02/2019/v1.0
     */
    @ApiOperation(value = "Actualiza a la BD, la Información enviada por el Json Bean de la Ubicacion del proyecto", authorizations = {@Authorization(value = "Token-PGC")})
    @PutMapping(value = UBICACIONES_ACT_ENDPOINT_EDIT, produces = "application/json; charset=UTF-8")
    public HashMap<String, Object> editActividadUbicacion(@ApiParam(value = "Identificador de la Ubicacion del Proyecto a Actualizar", required = true)
                                                          @PathVariable("idActividadUbicacion") long idActividadUbicacion,
                                                          @ApiParam(value = "Json de la nueva Ubicacion del Proyecto a Ingresar", required = true)
                                                          @RequestBody @Valid final TblActividadUbicacion _actividadUbicacionJson) throws Exception {
        // Ejecuta el try Cacth
        msgExceptions msgExeptions = new msgExceptions();

        // Fecha de Ingrso
        Date dateActual = new Date();

        try {
            // Busca la Ubicacion de la Actividad, desde el Reporsitorio con el Parametro del Json enviado ( "idUbicacionImplementacion")
            TblActividadUbicacion _tblActividadUbicacion = _actividadUbicacionRepository.findByIdActividadUbicacion(idActividadUbicacion);

            // Condiciona si tiene cambios
            if (_tblActividadUbicacion.getPorcentajeUbicacion() == _actividadUbicacionJson.getPorcentajeUbicacion()) {
                // No actualiza los datos que no se han modificado
                msgExeptions.map.put("findRecord", true);
                msgExeptions.map.put("findChange", false);
                msgMethod = "El registro de Ubicación no tiene cambios!!";

                return msgExeptions.msgJson(msgMethod, 200);
            } else {

                if (_actividadUbicacionRepository.countByIdActividadUbicacion(idActividadUbicacion) > 0) {
                    // Seteo de las Fecha y Hora de Creacion
                    _tblActividadUbicacion.setFechaModificacion(dateActual);
                    _tblActividadUbicacion.setHoraModificacion(dateActual);
                    _tblActividadUbicacion.setPorcentajeUbicacion(_actividadUbicacionJson.getPorcentajeUbicacion());


                    // Realizamos la Persistencia de los Datos
                    _actividadUbicacionRepository.save(_tblActividadUbicacion);
                    _actividadUbicacionRepository.flush();

                    // Busca la Ubicacion Recien Actualizada
                    msgExeptions.map.put("data", _actividadUbicacionRepository.findByIdActividadUbicacion(idActividadUbicacion));
                    msgExeptions.map.put("findRecord", true);
                    msgExeptions.map.put("findChange", true);

                    // Retorno de la Funcion
                    msgMethod = "La Ubicacion " + _actividadUbicacionJson.getCodigoActividad() + " para este Proyecto, se ha Actualizado de forma satisfactoria!!";

                    //Retorno del json
                    return msgExeptions.msgJson(msgMethod, 200);
                } else {
                    msgExeptions.map.put("findRecord", false);
                    msgExeptions.map.put("findChange", true);
                    msgMethod = "No Existe un registro con el Código de Ubicación para este Proyecto !!";

                    return msgExeptions.msgJson(msgMethod, 200);
                }
            }
        } catch (Exception ex) {
            msgMethod = "Ha Ocurrido un error al Intentar Actualizar, No existe un registro de Ubicación para este Proyecto!!";
            throw new SQLException("Se ha producido una excepción con el mensaje : " + msgMethod, ex);
        }
    } // FIN | editActividadUbicacion
}
