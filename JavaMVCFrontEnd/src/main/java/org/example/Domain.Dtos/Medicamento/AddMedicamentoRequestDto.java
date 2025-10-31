package org.example.Domain.Dtos.Medicamento;

public class AddMedicamentoRequestDto {
    private String nombre;
    private String presentacion;

    public AddMedicamentoRequestDto() {}

    public AddMedicamentoRequestDto(String nombre, String presentacion) {
        this.nombre = nombre;
        this.presentacion = presentacion;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
}

