package org.example.Domain.dtos.Medicamento;

public class UpdateMedicamentoRequestDto {
    private int id;
    private String nombre;
    private String presentacion;

    public UpdateMedicamentoRequestDto() {
    }

    public UpdateMedicamentoRequestDto(int id, String nombre, String presentacion) {
        this.id = id;
        this.nombre = nombre;
        this.presentacion = presentacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }
}

// Revisar