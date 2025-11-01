package org.example.Domain.dtos.Usuario;

import java.util.List;

public class ListUsuarioResponseDto {
    private List<UsuarioResponseDto> usuarios;

    public ListUsuarioResponseDto() {}

    public ListUsuarioResponseDto(List<UsuarioResponseDto> usuarios) {
        this.usuarios = usuarios;
    }

    public List<UsuarioResponseDto> getUsuarios() { return usuarios; }
    public void setUsuarios(List<UsuarioResponseDto> usuarios) { this.usuarios = usuarios; }
}

