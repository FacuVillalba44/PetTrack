package com.devfacu.pettrack.entidades;

import java.io.Serializable;

public class Usuario implements Serializable {
    private long id;
    private String nombre;
    private String email;
    private String password;

    // Constructores, getters y setters

    public Usuario() {
        // Constructor vacío
    }

    public Usuario(long id, String nombre, String email, String password) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // Getters y setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}