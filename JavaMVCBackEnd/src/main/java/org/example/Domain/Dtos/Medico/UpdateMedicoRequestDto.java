package org.example.Domain.Dtos.Medico;

public class UpdateMedicoRequestDto {
    private int id;
    private String nombre;
    private String clave;
    private String especialidad;

    public UpdateMedicoRequestDto() {}

    public UpdateMedicoRequestDto(int id, String nombre, String clave, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.clave = clave;
        this.especialidad = especialidad;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
