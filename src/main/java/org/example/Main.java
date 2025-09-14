package org.example;

import org.example.data_access_layer.UsuarioFileStore;
import org.example.domain_layer.Administrador;
import org.example.presentation_layer.Controllers.LoginController;
import org.example.presentation_layer.Views.LoginView;
import org.example.service_layer.UsuarioService;

import javax.swing.*;
import java.io.File;


public class Main {
     static void main() {
        UsuarioFileStore fileStore = new UsuarioFileStore(new File("usuarios.xml"));
        UsuarioService usuarioService = new UsuarioService(fileStore);
        LoginController controller = new LoginController(usuarioService);
        //agregar usuario admin si no existe
        if (usuarioService.leerPorId(1) == null) {
            usuarioService.agregar( new Administrador(1, "admin", "Admin User") );
        }


        SwingUtilities.invokeLater(() -> { new LoginView(controller); });
    }
}