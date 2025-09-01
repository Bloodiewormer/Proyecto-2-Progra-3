package org.example.domain_layer;
import jakarta.xml.bind.annotation.*;

@XmlAccessorType (XmlAccessType.FIELD)
@XmlSeeAlso({Usuario.class})

public class Persona {
    @XmlElement(name = "id")
    protected int id;
    @XmlElement(name = "nombre")
    protected String nombre;

    protected Persona() {}

    public Persona(int id, String nombre) {
    this.id = id;
    this.nombre = nombre;
    }

    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
