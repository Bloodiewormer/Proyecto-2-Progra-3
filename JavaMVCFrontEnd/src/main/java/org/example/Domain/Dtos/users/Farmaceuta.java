package org.example.Domain.Dtos.users;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "farmaceuta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Farmaceuta extends Usuario {

    protected Farmaceuta() {}
    public Farmaceuta(int id, String password, String nombre) {
        super(id, password, nombre);
    }

}
