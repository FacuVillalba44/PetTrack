package com.devfacu.pettrack.entidades;

import android.util.Log;

import java.io.Serializable;

public class Mascota implements Serializable {

    private int id_mascota;
    private String nombre;
    private String fecha_nacimiento;
    private String especie;
    private String raza;
    private String sexo;
    private String imagen;

    private byte [] imagen_blob;

    public int getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
    private int id_usuario;
    public byte[] getImagen_blob() {
        return imagen_blob;
    }

    public void setImagen_blob(byte[] imagen_blob) {
        this.imagen_blob = imagen_blob;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId_mascota() {
        Log.d("ID_MASCOTA_CLASS", "ID de la mascota: " + id_mascota);
        return id_mascota;
    }

    public void setId_mascota(int id_mascota) {
        this.id_mascota = id_mascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
