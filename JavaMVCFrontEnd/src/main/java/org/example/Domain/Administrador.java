package org.example.domain_layer;

import jakarta.xml.bind.annotation.*;

import java.text.MessageFormat;

@XmlRootElement(name = "administrador")
@XmlAccessorType(XmlAccessType.FIELD)
public class Administrador extends Usuario {

    @SuppressWarnings("unused")
    protected Administrador() {}
    @SuppressWarnings("unused")
    public Administrador(int id, String password, String nombre) {
        super(id, password, nombre);
    }

    @Override
    public String toString() {
        return MessageFormat.format("Administrador'{'id={0}, nombre=''{1}'''}'", id, nombre);
    }
}