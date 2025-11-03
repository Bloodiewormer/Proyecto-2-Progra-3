package org.example.Domain.Dtos.Administrador;


public class AdministradorResponseDto {
    private int id;
    private String nombre;

    public AdministradorResponseDto() {}

    public AdministradorResponseDto(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
