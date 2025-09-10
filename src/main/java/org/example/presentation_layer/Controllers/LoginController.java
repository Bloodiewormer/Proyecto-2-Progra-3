package org.example.presentation_layer.Controllers;

import com.sun.istack.localization.NullLocalizable;
import org.example.data_access_layer.IFileStore;
import org.example.data_access_layer.PacienteFileStore;
import org.example.data_access_layer.UsuarioFileStore;
import org.example.domain_layer.*;
import org.example.presentation_layer.Models.UserType;
import org.example.presentation_layer.Views.LoginView;
import org.example.presentation_layer.Views.MenuPrincipalView;
import org.example.service_layer.PacienteService;
import org.example.service_layer.UsuarioService;

import java.io.File;

public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public boolean login(int id, String password) {
        Usuario usuario = usuarioService.leerPorId(id);
        return usuario != null && usuario.getPassword().equals(password);
    }

    public boolean register(int id, String password, String nombre) {
        if (usuarioService.leerPorId(id) != null) return false;
        usuarioService.agregar(new Usuario(id, password, nombre));
        return true;
    }

    public boolean changePassword(int id, String newPassword) {
        Usuario usuario = usuarioService.leerPorId(id);
        if (usuario == null) return false;
        usuario.changePassword(newPassword);
        usuarioService.actualizar(usuario);
        return true;
    }

    public UserType getUserType(int id) {
        Usuario usuario = usuarioService.leerPorId(id);
        if (usuario == null) return UserType.NULL;
        else if (usuario instanceof Administrador) {
            return UserType.ADMINISTRADOR;
        } else if (usuario instanceof Farmaceuta) {
            return UserType.FARMACEUTA;
        } else if (usuario instanceof Medico) {
            return UserType.MEDICO;
        }
        return UserType.NULL;
    }

    public void onLoginSuccess(UserType userType, LoginView loginView) {
        loginView.setVisible(false);
        File usuariosFile = new File("usuarios.xml");
        File PacientesFile = new File("pacientes.xml");

        IFileStore<Usuario> fileStore = new UsuarioFileStore(usuariosFile);

        IFileStore<Paciente> fileStorePacientes = new PacienteFileStore(PacientesFile);
        UsuarioService usuarioService = new UsuarioService(fileStore);
        PacienteService pacienteService = new PacienteService(fileStorePacientes);

        MenuPrincipalView menu = new MenuPrincipalView(userType, this, usuarioService, pacienteService);
        menu.setVisible(true);
    }
}