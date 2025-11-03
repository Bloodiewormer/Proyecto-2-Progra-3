package org.example.Data;

import models.Mensaje;
import java.util.*;
import java.util.stream.Collectors;

public class MensajeDAO {
    private static MensajeDAO instancia;
    private Map<String, List<Mensaje>> mensajesPorUsuario;

    private MensajeDAO() {
        this.mensajesPorUsuario = new HashMap<>();
    }

    public static synchronized MensajeDAO getInstancia() {
        if (instancia == null) {
            instancia = new MensajeDAO();
        }
        return instancia;
    }

    public void guardarMensaje(Mensaje mensaje) {
        // Guardar para el destinatario
        mensajesPorUsuario
                .computeIfAbsent(mensaje.getDestinatarioId(), k -> new ArrayList<>())
                .add(mensaje);

        // También guardar para el remitente (opcional, para historial)
        mensajesPorUsuario
                .computeIfAbsent(mensaje.getRemitenteId(), k -> new ArrayList<>())
                .add(mensaje);
    }

    public List<Mensaje> obtenerMensajesNoLeidos(String usuarioId) {
        return mensajesPorUsuario.getOrDefault(usuarioId, new ArrayList<>())
                .stream()
                .filter(mensaje -> !mensaje.isLeido() &&
                        mensaje.getDestinatarioId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    public Mensaje obtenerMensajePorId(String mensajeId, String usuarioId) {
        return mensajesPorUsuario.getOrDefault(usuarioId, new ArrayList<>())
                .stream()
                .filter(m -> m.getId().equals(mensajeId))
                .findFirst()
                .orElse(null);
    }
}
