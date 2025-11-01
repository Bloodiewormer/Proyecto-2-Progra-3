package org.example.Domain.Dtos.models;
import jakarta.persistence.*;

@Entity
@Table(name = "administradores")
public class Administrador extends Usuario {

    public Administrador() {}

    public Administrador(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
    }
}
