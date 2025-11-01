package org.example.Domain.dtos.Paciente;

public class UpdatePacienteRequestDto {
    private int id;
    private String nombre;
    private String telefono;
    private String fechaNacimiento;

    public UpdatePacienteRequestDto() {}

    public UpdatePacienteRequestDto(int id, String nombre, String telefono, String fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}
