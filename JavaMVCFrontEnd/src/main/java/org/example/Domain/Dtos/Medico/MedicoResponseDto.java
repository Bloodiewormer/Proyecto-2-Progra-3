package org.example.Domain.Dtos.Medico;

public class MedicoResponseDto {
    private int id;
    private String nombre;
    private String especialidad;

    public MedicoResponseDto() {}

    public MedicoResponseDto(int id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}

