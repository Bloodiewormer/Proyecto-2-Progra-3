package org.example.Domain.Dtos.models;
import jakarta.persistence.*;

@Entity
@Table(name = "farmaceutas")
public class Farmaceuta extends Usuario {

    public Farmaceuta() {}

    public Farmaceuta(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
    }
}