package org.example.Domain.dtos.Paciente;

public class AddPacienteRequestDto {
    private String nombre;
    private String telefono;
    private String fechaNacimiento;

    public AddPacienteRequestDto() {}

    public AddPacienteRequestDto(String nombre, String telefono, String fechaNacimiento) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}
