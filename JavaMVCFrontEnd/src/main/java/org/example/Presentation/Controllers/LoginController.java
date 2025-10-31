package org.example.Presentation.Controllers;


import org.example.data_access_layer.*;
import org.example.domain_layer.*;
import org.example.presentation_layer.Models.UserType;
import org.example.presentation_layer.Views.CambioClaveView;
import org.example.presentation_layer.Views.LoginView;
import org.example.presentation_layer.Views.MenuPrincipalView;
import org.example.Services.MedicamentoService;
import org.example.Services.PacienteService;
import org.example.Services.RecetaService;
import org.example.Services.UsuarioService;

import java.io.File;

@SuppressWarnings("ClassCanBeRecord")
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public boolean login(int id, String password) {
        Usuario usuario = usuarioService.leerPorId(id);
        return usuario != null && usuario.getPassword().equals(password);
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
        return switch (usuario) {
            case Administrador e -> UserType.ADMINISTRADOR;
            case Farmaceuta e -> UserType.FARMACEUTA;
            case Medico e -> UserType.MEDICO;
            case null, default -> UserType.NULL;
        };
    }

    public void onLoginSuccess(UserType userType, LoginView loginView, int userId) {
        loginView.setVisible(false);
        File usuariosFile = new File("usuarios.xml");
        File PacientesFile = new File("pacientes.xml");
        File MedicamentosFile = new File("medicamentos.xml");
        File RecetasFile = new File("recetas.xml");

        IFileStore<Usuario> fileStore = new UsuarioFileStore(usuariosFile);
        IFileStore<Paciente> fileStorePacientes = new PacienteFileStore(PacientesFile);
        IFileStore<Medicamento> fileStoreMedicamentos = new MedicamentoFileStore( MedicamentosFile);
        IFileStore <Receta> fileStoreRecetas = new RecetaFileStore(RecetasFile);

        UsuarioService usuarioService = new UsuarioService(fileStore);
        PacienteService pacienteService = new PacienteService(fileStorePacientes);
        MedicamentoService medicamentoService = new MedicamentoService(fileStoreMedicamentos);
        RecetaService recetaService = new RecetaService(fileStoreRecetas);

        MenuPrincipalView menu = new MenuPrincipalView(userType, this, usuarioService, pacienteService, medicamentoService,recetaService ,userId);
        menu.setVisible(true);
    }

    public void showPasswordChangeView() {

        new CambioClaveView(this);



    }

    public void exitApplication() {
        System.exit(0);
    }
}