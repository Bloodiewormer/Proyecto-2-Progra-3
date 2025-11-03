package org.example.API.Controllers;

import services.GestionUsuariosService;
import dtos.UsuarioActivoDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @GetMapping("/conectados")
    public List<UsuarioActivoDTO> obtenerUsuariosConectados() {
        return GestionUsuariosService.getInstancia().obtenerUsuariosConectados();
    }
}
