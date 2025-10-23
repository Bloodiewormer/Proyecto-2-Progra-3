package org.example.domain_layer;

import jakarta.xml.bind.annotation.*;

import java.text.MessageFormat;

@XmlRootElement(name = "usuario")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Administrador.class, Medico.class, Farmaceuta.class})
public class Usuario extends  Persona {
    @XmlElement(name = "password")
    protected String password;

    protected Usuario() {}

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

    @Override
    public String toString() {
        return MessageFormat.format("Usuario'{'id={0}, nombre=''{1}'', password=''{2}'''}'", id, nombre, password);
    }
}
