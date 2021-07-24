package com.example.denuncia.model;

public class Tipo_denuncia {

    public String nombre;

    public Tipo_denuncia(){

    }

    public Tipo_denuncia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
