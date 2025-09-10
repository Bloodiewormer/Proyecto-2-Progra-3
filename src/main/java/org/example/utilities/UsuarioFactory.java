package org.example.utilities;

import org.example.domain_layer.*;

public class UsuarioFactory {

    public static Usuario crearUsuario(UserType tipo, int id, String password, String nombre, String extra) {
        return switch (tipo) {
            case ADMINISTRADOR -> new Administrador(id, password, nombre);
            case MEDICO -> new Medico(id, password, nombre, extra); // extra = especialidad
            case FARMACEUTA -> new Farmaceuta(id, password, nombre);
            default -> throw new IllegalArgumentException("Tipo de usuario no válido: " + tipo);
        };
    }
}

