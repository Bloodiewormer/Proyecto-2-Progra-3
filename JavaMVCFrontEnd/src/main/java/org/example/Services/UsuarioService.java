package org.example.Services;

import org.example.Domain.Dtos.Usuario.UsuarioResponseDto;
import org.example.Domain.Dtos.Usuario.AddUsuarioRequestDto;
import org.example.Domain.Dtos.Usuario.UpdateUsuarioRequestDto;
import org.example.Utilities.ChangeType;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private final List<UsuarioResponseDto> usuarios = new ArrayList<>();
    private final List<IServiceObserver<UsuarioResponseDto>> observers = new ArrayList<>();

    public void agregar(AddUsuarioRequestDto dto) {
        int newId = usuarios.size() + 1;
        UsuarioResponseDto nuevo = new UsuarioResponseDto(newId, dto.getNombre());
        usuarios.add(nuevo);
        notifyObservers(ChangeType.CREATED, nuevo);
    }

    public void actualizar(UpdateUsuarioRequestDto dto) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == dto.getId()) {
                usuarios.set(i, new UsuarioResponseDto(dto.getId(), dto.getNombre()));
                notifyObservers(ChangeType.UPDATED, usuarios.get(i));
                return;
            }
        }
        throw new IllegalArgumentException("Usuario no encontrado");
    }

    public void borrar(int id) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == id) {
                UsuarioResponseDto eliminado = usuarios.remove(i);
                notifyObservers(ChangeType.DELETED, eliminado);
                return;
            }
        }
    }

    public List<UsuarioResponseDto> leerTodos() {
        return new ArrayList<>(usuarios);
    }

    public UsuarioResponseDto leerPorId(int id) {
        for (UsuarioResponseDto u : usuarios) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    public void addObserver(IServiceObserver<UsuarioResponseDto> observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    private void notifyObservers(ChangeType type, UsuarioResponseDto entity) {
        for (IServiceObserver<UsuarioResponseDto> obs : observers) {
            obs.onDataChanged(type, entity);
        }
    }
}

