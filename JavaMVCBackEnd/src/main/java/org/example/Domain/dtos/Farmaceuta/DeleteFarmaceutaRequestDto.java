package org.example.Domain.Dtos.Farmaceuta;

public class DeleteFarmaceutaRequestDto {
    private int id;

    public DeleteFarmaceutaRequestDto() {}

    public DeleteFarmaceutaRequestDto(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
