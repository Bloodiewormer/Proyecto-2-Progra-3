package org.example.Domain.Dtos.Administrador;

import java.util.List;

public class ListAdministradorResponseDto {
    private List<AdministradorResponseDto> administradores;

    public ListAdministradorResponseDto() {}

    public ListAdministradorResponseDto(List<AdministradorResponseDto> administradores) {
        this.administradores = administradores;
    }

    public List<AdministradorResponseDto> getAdministradores() { return administradores; }
    public void setAdministradores(List<AdministradorResponseDto> administradores) { this.administradores = administradores; }
}
