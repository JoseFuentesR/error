package com.example.denuncia.model;

import java.text.SimpleDateFormat;

public class Denuncia {

    private String tipo_denuncia;
    private String titulo_denuncia;
    private String estado_denuncia;
    private String fecha;
    private String denuncia_detalles;
    private String direccion;
    private String url;

    public Denuncia(){

    }

    public Denuncia(String tipo_denuncia, String titulo_denuncia, String estado_denuncia, String fecha, String denuncia_detalles, String direccion, String url) {
        this.tipo_denuncia = tipo_denuncia;
        this.titulo_denuncia = titulo_denuncia;
        this.estado_denuncia = estado_denuncia;
        this.fecha = fecha;
        this.denuncia_detalles = denuncia_detalles;
        this.direccion = direccion;
        this.url = url;
    }

    public String getTipo_denuncia() {
        return tipo_denuncia;
    }

    public void setTipo_denuncia(String tipo_denuncia) {
        this.tipo_denuncia = tipo_denuncia;
    }

    public String getTitulo_denuncia() {
        return titulo_denuncia;
    }

    public void setTitulo_denuncia(String titulo_denuncia) {
        this.titulo_denuncia = titulo_denuncia;
    }

    public String getEstado_denuncia() {
        return estado_denuncia;
    }

    public void setEstado_denuncia(String estado_denuncia) {
        this.estado_denuncia = estado_denuncia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDenuncia_detalles() {
        return denuncia_detalles;
    }

    public void setDenuncia_detalles(String denuncia_detalles) {
        this.denuncia_detalles = denuncia_detalles;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}