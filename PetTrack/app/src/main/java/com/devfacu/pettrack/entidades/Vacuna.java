package com.devfacu.pettrack.entidades;

import java.io.Serializable;
import java.time.LocalDate;

public class Vacuna implements Serializable {

    private int id_vacuna;
    private String nombre_vacuna;
    private String fecha_aplicacion;
    private String proxima_aplicacion;



    private int id_mascota;
    public Vacuna() {
    }


    public int getId_vacuna() {
        return id_vacuna;
    }

    public void setId_vacuna(int id_vacuna) {
        this.id_vacuna = id_vacuna;
    }

    public String getNombre_vacuna() {
        return nombre_vacuna;
    }

    public void setNombre_vacuna(String nombre_vacuna) {
        this.nombre_vacuna = nombre_vacuna;
    }

    public String getFecha_aplicacion() {
        return fecha_aplicacion;
    }

    public void setFecha_aplicacion(String fecha_aplicacion) {
        this.fecha_aplicacion = fecha_aplicacion;
    }

    public String getProxima_aplicacion() {
        return proxima_aplicacion;
    }

    public void setProxima_aplicacion(String proxima_aplicacion) {
        this.proxima_aplicacion = proxima_aplicacion;
    }

    public int getId_mascota() {
        return id_mascota;
    }

    public void setId_mascota(int id_mascota) {
        this.id_mascota = id_mascota;
    }

}
