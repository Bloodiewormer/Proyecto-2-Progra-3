package org.example.domain_layer;

import jakarta.xml.bind.annotation.*;

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
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
