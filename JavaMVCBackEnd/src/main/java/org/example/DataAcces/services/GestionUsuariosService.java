package org.example.DataAcces.services;

import models.UsuarioActivo;
import dtos.UsuarioActivoDTO;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GestionUsuariosService {
    private static GestionUsuariosService instancia;
    private Map<String, UsuarioActivo> usuariosConectados;
    private List<Runnable> listeners;

    private GestionUsuariosService() {
        this.usuariosConectados = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
    }

    public static synchronized GestionUsuariosService getInstancia() {
        if (instancia == null) {
            instancia = new GestionUsuariosService();
        }
        return instancia;
    }

    public void conectarUsuario(String usuarioId, String nombre) {
        UsuarioActivo usuario = new UsuarioActivo(usuarioId, nombre);
        usuariosConectados.put(usuarioId, usuario);
        notificarCambios();
    }

    public void desconectarUsuario(String usuarioId) {
        UsuarioActivo usuario = usuariosConectados.get(usuarioId);
        if (usuario != null) {
            usuario.setConectado(false);
            usuariosConectados.remove(usuarioId);
            notificarCambios();
        }
    }

    public List<UsuarioActivoDTO> obtenerUsuariosConectados() {
        return usuariosConectados.values()
                .stream()
                .map(u -> new UsuarioActivoDTO(u.getId(), u.getNombre(), u.isConectado()))
                .collect(Collectors.toList());
    }

    public void agregarListener(Runnable listener) {
        listeners.add(listener);
    }

    private void notificarCambios() {
        listeners.forEach(Runnable::run);
    }
}
