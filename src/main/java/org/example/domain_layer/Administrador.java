package org.example.domain_layer;

import jakarta.xml.bind.annotation.*;


@XmlRootElement(name = "administrador")
@XmlAccessorType(XmlAccessType.FIELD)
public class Administrador extends Usuario {

    protected Administrador() {}


    public Administrador(int id, String password, String nombre) {
        super(id, password, nombre);
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
