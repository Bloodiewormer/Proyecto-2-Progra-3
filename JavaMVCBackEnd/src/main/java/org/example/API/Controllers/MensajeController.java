package org.example.API.Controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.DataAcces.services.MensajeService;
import org.example.DataAcces.services.UsuarioService;
import org.example.Domain.Dtos.Mensaje.MensajeResponseDto;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.models.Mensaje;
import org.example.Domain.models.Usuario;
import org.example.Utilities.LocalDateTimeAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de mensajería para el SocketServer (Puerto 7000)
 * Maneja peticiones REST-like de mensajes
 */
public class MensajeController {
    private final MensajeService mensajeService;
    private final UsuarioService usuarioService;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public MensajeController(MensajeService mensajeService, UsuarioService usuarioService) {
        this.mensajeService = mensajeService;
        this.usuarioService = usuarioService;
    }

    /**
     * Enruta las peticiones a sus métodos correspondientes
     */
    public ResponseDto route(RequestDto request) {
        try {
            String action = request.getRequest();

            if (action == null) {
                return new ResponseDto(false, "Acción no especificada", null);
            }

            return switch (action) {
                case "historial", "list" -> handleObtenerHistorial(request);
                case "noLeidos" -> handleObtenerNoLeidos(request);
                case "marcarLeido" -> handleMarcarLeido(request);
                default -> new ResponseDto(false, "Acción no reconocida: " + action, null);
            };
        } catch (Exception e) {
            System.err.println("[MensajeController] ❌ Error en route: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error procesando solicitud: " + e.getMessage(), null);
        }
    }

    /**
     * Maneja la obtención del historial de mensajes entre dos usuarios
     */
    private ResponseDto handleObtenerHistorial(RequestDto request) {
        try {
            HistorialRequest dto = gson.fromJson(request.getData(), HistorialRequest.class);

            if (dto.usuario1 == null || dto.usuario2 == null) {
                return new ResponseDto(false, "Se requieren ambos nombres de usuario", null);
            }

            Usuario user1 = usuarioService.getUserByNombre(dto.usuario1);
            Usuario user2 = usuarioService.getUserByNombre(dto.usuario2);

            if (user1 == null || user2 == null) {
                return new ResponseDto(false, "Usuario no encontrado", null);
            }

            List<Mensaje> mensajes = mensajeService.obtenerHistorial(user1.getId(), user2.getId());
            List<MensajeResponseDto> responseDtos = new ArrayList<>();

            for (Mensaje mensaje : mensajes) {
                Usuario sender = usuarioService.getUserById(mensaje.getSenderId());
                Usuario recipient = usuarioService.getUserById(mensaje.getRecipientId());

                if (sender != null && recipient != null) {
                    responseDtos.add(new MensajeResponseDto(
                            mensaje.getId(),
                            sender.getNombre(),
                            recipient.getNombre(),
                            mensaje.getMessage(),
                            mensaje.getStatus(),
                            mensaje.getCreatedAt() != null ? mensaje.getCreatedAt().toString() : null,  // ✅ Convertir a String
                            mensaje.getReadAt() != null ? mensaje.getReadAt().toString() : null         // ✅ Convertir a String
                    ));
                }
            }

            System.out.println("[MensajeController] ✅ Historial recuperado: " + responseDtos.size() + " mensajes");
            return new ResponseDto(true, "Historial obtenido", gson.toJson(responseDtos));

        } catch (Exception e) {
            System.err.println("[MensajeController] ❌ Error en handleObtenerHistorial: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error obteniendo historial: " + e.getMessage(), null);
        }
    }

    /**
     * Maneja la obtención de mensajes no leídos
     */
    private ResponseDto handleObtenerNoLeidos(RequestDto request) {
        try {
            String username = gson.fromJson(request.getData(), String.class);

            if (username == null) {
                return new ResponseDto(false, "Nombre de usuario requerido", null);
            }

            Usuario usuario = usuarioService.getUserByNombre(username);
            if (usuario == null) {
                return new ResponseDto(false, "Usuario no encontrado", null);
            }

            List<Mensaje> mensajes = mensajeService.obtenerMensajesNoLeidos(usuario.getId());
            List<MensajeResponseDto> responseDtos = new ArrayList<>();

            for (Mensaje mensaje : mensajes) {
                Usuario sender = usuarioService.getUserById(mensaje.getSenderId());
                Usuario recipient = usuarioService.getUserById(mensaje.getRecipientId());

                if (sender != null && recipient != null) {
                    responseDtos.add(new MensajeResponseDto(
                            mensaje.getId(),
                            sender.getNombre(),
                            recipient.getNombre(),
                            mensaje.getMessage(),
                            mensaje.getStatus(),
                            mensaje.getCreatedAt() != null ? mensaje.getCreatedAt().toString() : null,
                            mensaje.getReadAt() != null ? mensaje.getReadAt().toString() : null
                    ));
                }
            }

            return new ResponseDto(true, "Mensajes no leídos obtenidos", gson.toJson(responseDtos));

        } catch (Exception e) {
            System.err.println("[MensajeController] ❌ Error en handleObtenerNoLeidos: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo mensajes: " + e.getMessage(), null);
        }
    }

    /**
     * Maneja marcar un mensaje como leído
     */
    private ResponseDto handleMarcarLeido(RequestDto request) {
        try {
            Long mensajeId = gson.fromJson(request.getData(), Long.class);

            if (mensajeId == null) {
                return new ResponseDto(false, "ID de mensaje requerido", null);
            }

            mensajeService.marcarComoLeido(mensajeId);
            return new ResponseDto(true, "Mensaje marcado como leído", null);

        } catch (Exception e) {
            System.err.println("[MensajeController] ❌ Error en handleMarcarLeido: " + e.getMessage());
            return new ResponseDto(false, "Error marcando mensaje: " + e.getMessage(), null);
        }
    }

    // DTO interno para historial
    private static class HistorialRequest {
        String usuario1;
        String usuario2;
    }
}
