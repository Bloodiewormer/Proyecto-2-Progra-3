package org.example.Domain.DetalleReceta;

public class AddDetalleRecetaRequestDto {
    private int idMedicamento;
    private int cantidad;
    private String indicaciones;
    private int dias;

    public AddDetalleRecetaRequestDto() {}

    public AddDetalleRecetaRequestDto(int idMedicamento, int cantidad, String indicaciones, int dias) {
        this.idMedicamento = idMedicamento;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
        this.dias = dias;
    }

    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public int getDias() { return dias; }
    public void setDias(int dias) { this.dias = dias; }
}
