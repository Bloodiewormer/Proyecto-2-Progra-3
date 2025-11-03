package org.example.Presentation.Controllers;

import org.example.Domain.Dtos.Auth.UserResponseDto;
import org.example.Presentation.Models.UserType;
import org.example.Presentation.Observable;
import org.example.Presentation.Views.CambioClaveView;
import org.example.Presentation.Views.LoginView;
import org.example.Presentation.Views.MenuPrincipalView;
import org.example.Services.*;
import org.example.Utilities.ChangeType;

import javax.swing.*;

public class LoginController extends Observable {

    private final LoginView loginView;
    private final AuthService authService;
    private UserResponseDto currentUser;

    // Server configuration
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 7000;

    public LoginController(LoginView loginView, AuthService authService) {
        this.loginView = loginView;
        this.authService = authService;

        addObserver(loginView);
        wireEvents();
    }

    private void wireEvents() {
        loginView.addLoginListener(e -> handleLogin());
        loginView.setPasswordChangeCallback(this::showPasswordChangeView);
    }

    private void handleLogin() {
        String userIdText = loginView.getUsername(); // ✅ Campo que contiene el ID
        String password = loginView.getPassword();

        // Validar que el ID no esté vacío
        if (userIdText == null || userIdText.trim().isEmpty()) {
            showError("El ID de usuario es requerido.");
            return;
        }

        // Validar que la contraseña no esté vacía
        if (password == null || password.trim().isEmpty()) {
            showError("La contraseña es requerida.");
            return;
        }

        // Parsear el ID de usuario como int
        int userId;
        try {
            userId = Integer.parseInt(userIdText.trim());
        } catch (NumberFormatException ex) {
            showError("ID de usuario inválido.\nDebe ser un número entero positivo.");
            return;
        }

        // Validar que el ID sea positivo
        if (userId <= 0) {
            showError("El ID de usuario debe ser mayor a 0.");
            return;
        }

        loginView.showLoading(true);

        SwingWorker<UserResponseDto, Void> worker = new SwingWorker<>() {
            @Override
            protected UserResponseDto doInBackground() throws Exception {
                return authService.login(userId, password).get();
            }

            @Override
            protected void done() {
                loginView.showLoading(false);
                try {
                    UserResponseDto user = get();
                    if (user != null) {
                        currentUser = user;
                        onLoginSuccess(user);
                    } else {
                        showError("Credenciales inválidas o usuario inactivo.\n\n" +
                                "Verifique:\n" +
                                "• ID de usuario correcto\n" +
                                "• Contraseña correcta\n" +
                                "• Usuario activo en el sistema");
                    }
                } catch (Exception ex) {
                    handleError("Error al intentar iniciar sesión", ex);
                }
            }
        };
        worker.execute();
    }


    private void onLoginSuccess(UserResponseDto user) {
        // Notify observers
        notifyObservers(ChangeType.UPDATED, user);

        // Hide login view
        loginView.setVisible(false);

        // Determine user type and open main view
        UserType userType = determineUserType(user.getRole());
        openMainView(userType, user);
    }


    private UserType determineUserType(String role) {
        if (role == null) return UserType.NULL;

        return switch (role.toUpperCase()) {
            case "ADMIN", "ADMINISTRADOR" -> UserType.ADMINISTRADOR;
            case "FARMACEUTA" -> UserType.FARMACEUTA;
            case "MEDICO", "DOCTOR" -> UserType.MEDICO;
            default -> UserType.NULL;
        };
    }

    private void openMainView(UserType userType, UserResponseDto user) {
        // Initialize services
        UsuarioService usuarioService = new UsuarioService(SERVER_HOST, SERVER_PORT);
        PacienteService pacienteService = new PacienteService(SERVER_HOST, SERVER_PORT);
        MedicamentoService medicamentoService = new MedicamentoService(SERVER_HOST, SERVER_PORT);
        DashboardService dashboardService = new DashboardService(SERVER_HOST, SERVER_PORT);
        PrescribirService prescribirService = new PrescribirService(SERVER_HOST, SERVER_PORT);
        DespachoService despachoService = new DespachoService(SERVER_HOST, SERVER_PORT);

        // Create and show main view
        MenuPrincipalView mainView = new MenuPrincipalView(
                userType,
                this, // LoginController
                usuarioService,
                pacienteService,
                medicamentoService,
                dashboardService,
                prescribirService,
                despachoService ,
                user.getId().intValue(),
                user
        );

        mainView.setVisible(true);
    }


    public void showPasswordChangeView() {
        AuthService passwordAuthService = new AuthService(SERVER_HOST, SERVER_PORT);

        new CambioClaveView(passwordAuthService);
    }

    public void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                loginView,
                "¿Está seguro que desea salir?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void logout() {
        currentUser = null;
        loginView.clearFields();
        loginView.setVisible(true);
    }

    public UserResponseDto getCurrentUser() {
        return currentUser;
    }

    // Validation
    private boolean validateInput(String userId, String password) {
        if (userId == null || userId.trim().isEmpty()) {
            showError("El ID de usuario es requerido.");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            showError("La contraseña es requerida.");
            return false;
        }

        return true;
    }

    // UI Helper Methods
    private void showError(String message) {
        JOptionPane.showMessageDialog(loginView, message, "Error de Login", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                loginView,
                message,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void handleError(String message, Exception ex) {
        System.err.println(message + ": " + ex.getMessage());
        ex.printStackTrace();
        showError(message + "\n" + ex.getMessage());
    }

}