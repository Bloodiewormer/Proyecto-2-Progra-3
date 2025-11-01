package org.example.Domain.Dtos.models;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicos")
public class Medico extends Usuario {

    @Column(nullable = false)
    private String especialidad;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL)
    private List<Receta> recetas = new ArrayList<>();

    public Medico() {}

    public Medico(String nombre, String clave, String especialidad) {
        this.nombre = nombre;
        this.clave = clave;
        this.especialidad = especialidad;
    }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public List<Receta> getRecetas() { return recetas; }
    public void setRecetas(List<Receta> recetas) { this.recetas = recetas; }
}