package com.appsmoviles.cuartaappu3.modelos;

public class Contacto {
    private String telefono;
    private String nombre;
    private String ciudad;

    public Contacto(String telefono, String nombre, String ciudad) {
        this.telefono = telefono;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
