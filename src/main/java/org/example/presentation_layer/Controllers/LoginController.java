package org.example.presentation_layer.Controllers;

import com.sun.istack.localization.NullLocalizable;
import org.example.domain_layer.Administrador;
import org.example.domain_layer.Farmaceuta;
import org.example.domain_layer.Medico;
import org.example.domain_layer.Usuario;
import org.example.presentation_layer.Models.UserType;
import org.example.presentation_layer.Views.LoginView;
import org.example.presentation_layer.Views.MenuPrincipalView;
import org.example.service_layer.UsuarioService;

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
        // Hide login view
        loginView.setVisible(false);
        // Show menu principal view
        MenuPrincipalView menuPrincipalView = new MenuPrincipalView(userType, this);
        menuPrincipalView.setVisible(true);
    }
}