package org.example.domain_layer;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "medico")
@XmlAccessorType(XmlAccessType.FIELD)
public class Medico extends Usuario {
    @XmlElement(name = "especialidad")
    private String especialidad;

    protected Medico() {}

    public Medico(int id, String password, String nombre, String especialidad) {
        super(id, password, nombre);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {return especialidad;}
}
