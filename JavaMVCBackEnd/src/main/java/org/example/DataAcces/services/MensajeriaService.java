package org.example.DataAcces.services;

import Data.MensajeDAO;
import models.Mensaje;
import dtos.MensajeDTO;
import java.util.List;
import java.util.stream.Collectors;

public class MensajeriaService {
    private static MensajeriaService instancia;
    private MensajeDAO mensajeDAO;

    private MensajeriaService() {
        this.mensajeDAO = MensajeDAO.getInstancia();
    }

    public static synchronized MensajeriaService getInstancia() {
        if (instancia == null) {
            instancia = new MensajeriaService();
        }
        return instancia;
    }

    public Mensaje enviarMensaje(String remitenteId, String destinatarioId, String contenido) {
        Mensaje mensaje = new Mensaje(remitenteId, destinatarioId, contenido);
        mensajeDAO.guardarMensaje(mensaje);
        return mensaje;
    }

    public List<MensajeDTO> obtenerMensajesNoLeidos(String usuarioId) {
        return mensajeDAO.obtenerMensajesNoLeidos(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public MensajeDTO recibirMensaje(String mensajeId, String usuarioId) {
        Mensaje mensaje = mensajeDAO.obtenerMensajePorId(mensajeId, usuarioId);
        if (mensaje != null && !mensaje.isLeido()) {
            mensaje.marcarComoLeido();
            return convertirADTO(mensaje);
        }
        return null;
    }

    private MensajeDTO convertirADTO(Mensaje mensaje) {
        MensajeDTO dto = new MensajeDTO();
        dto.setId(mensaje.getId());
        dto.setRemitenteId(mensaje.getRemitenteId());
        dto.setDestinatarioId(mensaje.getDestinatarioId());
        dto.setContenido(mensaje.getContenido());
        dto.setFechaEnvio(mensaje.getFechaEnvio());
        dto.setLeido(mensaje.isLeido());
        return dto;
    }
}
