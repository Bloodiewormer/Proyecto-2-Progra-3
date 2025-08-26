package org.example.domain_layer;

public class Usuario {
    protected int id;
    protected String password;
    protected String nombre;

    public Usuario(int id, String password, String nombre) {
        this.id = id;
        this.password = password;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
