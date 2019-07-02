package com.rosario.hp.espaciojahiel.include;

public class Constantes {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;

    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    private static final String PUERTO_HOST = "";


    private static final String IP = "app.espaciojahiel.com";
    /**
     * URLs del Web Service
     */

    /*USUARIOS*/
    public static final String INSERT_USUARIO =  "https://" + IP + PUERTO_HOST + "/app/agregar_usuario.php";
    public static final String UPDATE_USUARIO =  "https://" + IP + PUERTO_HOST + "/app/actualizar_usuario.php";
    public static final String INSERT_USUARIO_MENSAJE =  "https://" + IP + PUERTO_HOST + "/app/agregar_usuario_mensaje.php";
    public static final String INSERT_USUARIO_IMAGEN =  "https://" + IP + PUERTO_HOST + "/app/agregar_usuario_imagen.php";
    public static final String INSERT_USUARIO_FECHA =  "https://" + IP + PUERTO_HOST + "/app/agregar_mensaje_fecha.php";
    public static final String GET_BY_CLAVE =  "https://" + IP + PUERTO_HOST + "/app/obtener_clave.php";
    public static final String GET_BY_ID_USUARIO =  "https://" + IP + PUERTO_HOST + "/app/obtener_un_usuario.php";
    public static final String UPDATE_TOKEN = "https://" + IP + PUERTO_HOST + "/app/actualizar_token.php";
    public static final String VERIFICAR_MENSAJE = "https://" + IP + PUERTO_HOST + "/app/verificar_mensaje_usuario.php";
    public static final String VERIFICAR_MENSAJE_FECHA = "https://" + IP + PUERTO_HOST + "/app/verificar_mensaje_fecha.php";
    public static final String GET_MENSAJES_USUARIO = "https://" + IP + PUERTO_HOST + "/app/obtener_mensajes_usuario.php";
    public static final String GET_MENSAJES_FECHA = "https://" + IP + PUERTO_HOST + "/app/obtener_mensaje_fecha.php";

    /*EVENTOS*/
    public static final String GET_EVENTOS =  "https://" + IP + PUERTO_HOST + "/app/obtener_eventos.php";

    /*Notificaciones*/
    public static final String INSERT_NOTIFICACION =  "https://" + IP + PUERTO_HOST + "/app/agregar_notificacion.php";
    public static final String GET_NOTIFICACIONES =  "https://" + IP + PUERTO_HOST + "/app/obtener_notificacion.php";

    /*ESPACIOS AMIGOS*/
    public static final String GET_ESPACIOS =  "https://" + IP + PUERTO_HOST + "/app/obtener_espacios.php";

    /*Imagenes*/
    public static final String GET_IMAGENES =  "https://" + IP + PUERTO_HOST + "/app/obtener_imagenes.php";
    public static final String GET_IMAGENES_USUARIO =  "https://" + IP + PUERTO_HOST + "/app/obtener_imagenes_usuario.php";

    /*ARCANGEL*/
    public static final String GET_ARCANGEL_FECHA =  "https://" + IP + PUERTO_HOST + "/app/obtener_arcangel_fecha.php";

    /*Mensajes*/
    public static final String GET_MENSAJE =  "https://" + IP + PUERTO_HOST + "/app/obtener_un_mensaje.php";
}
