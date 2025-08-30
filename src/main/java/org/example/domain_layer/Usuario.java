package org.example.domain_layer;

public class Usuario extends  Persona {
    protected String password;

    public Usuario(int id, String password, String nombre) {
        super(id, nombre);
        this.password = password;

    }

    public String getPassword() {
        return password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
