package org.example.Domain.Dtos.Medico;

public class UpdateMedicoRequestDto {
    private int id;
    private String nombre;
    private String password;
    private String especialidad;

    public UpdateMedicoRequestDto() {}

    public UpdateMedicoRequestDto(int id, String nombre, String password, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
        this.especialidad = especialidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
