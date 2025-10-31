package org.example.Domain.Dtos.Medico;

public class AddMedicoRequestDto {
    private String nombre;
    private String password;
    private String especialidad;

    public AddMedicoRequestDto() {}

    public AddMedicoRequestDto(String nombre, String password, String especialidad) {
        this.nombre = nombre;
        this.password = password;
        this.especialidad = especialidad;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}


