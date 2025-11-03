package org.example.Domain.dtos;

public class UsuarioActivoDTO {
    private String id;
    private String nombre;
    private boolean conectado;

    // Constructores, Getters y Setters
    public UsuarioActivoDTO(String id, String nombre, boolean conectado) {
        this.id = id;
        this.nombre = nombre;
        this.conectado = conectado;
    }

    // Getters y Setters...
}
