package org.example.domain_layer;

public class Medico extends Usuario {
    private String especialidad;

    public Medico(int id, String password, String nombre, String especialidad) {
        super(id, password, nombre);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

}
