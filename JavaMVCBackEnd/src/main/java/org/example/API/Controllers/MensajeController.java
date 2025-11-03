package org.example.API.Controllers;

import services.MensajeriaService;
import dtos.MensajeDTO;
// import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
public class MensajeController {

    @PostMapping("/enviar")
    public void enviarMensaje(@RequestBody MensajeDTO mensajeDTO) {
        MensajeriaService.getInstancia().enviarMensaje(
                mensajeDTO.getRemitenteId(),
                mensajeDTO.getDestinatarioId(),
                mensajeDTO.getContenido()
        );
    }

    @GetMapping("/no-leidos/{usuarioId}")
    public List<MensajeDTO> obtenerMensajesNoLeidos(@PathVariable String usuarioId) {
        return MensajeriaService.getInstancia().obtenerMensajesNoLeidos(usuarioId);
    }

    @PostMapping("/recibir/{mensajeId}/{usuarioId}")
    public MensajeDTO recibirMensaje(@PathVariable String mensajeId, @PathVariable String usuarioId) {
        return MensajeriaService.getInstancia().recibirMensaje(mensajeId, usuarioId);
    }
}
