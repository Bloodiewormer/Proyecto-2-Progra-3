package org.example.Presentation.Controllers;

import org.example.Presentation.Views.CambioClaveView;
import org.example.Services.AuthService;

import javax.swing.*;

public class CambioClaveController {

    private final CambioClaveView view;
    private final AuthService authService;
    private final Integer fixedUserId;

    public CambioClaveController(CambioClaveView view, AuthService authService) {
        this(view, authService, null);
    }

    public CambioClaveController(CambioClaveView view, AuthService authService, Integer userId) {
        this.view = view;
        this.authService = authService;
        this.fixedUserId = userId;
        wire();
    }

    private void wire() {
        view.getAcceptButton().addActionListener(e -> onAccept());
    }

    private void onAccept() {
        String idText = view.getIdTextField().getText().trim();
        String newPass = new String(view.getPasswordField().getPassword());
        String confirm = new String(view.getConfirmPasswordField().getPassword());

        // Validar ID de usuario
        int userId;
        try {
            userId = fixedUserId != null ? fixedUserId : Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            showError("ID de usuario inválido");
            return;
        }

        // Validar formato de contraseña
        if (!isValidPassword(newPass)) {
            showError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        // Validar confirmación
        if (!newPass.equals(confirm)) {
            showError("Las contraseñas no coinciden");
            return;
        }

        // Ejecutar cambio en background
        view.showLoading(true);
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // 🔧 FIX: Llamar al backend con contraseña vacía como "current"
                // El backend debe permitir esto para "primer cambio"
                return authService.changePassword(userId, "", newPass).get();
            }

            @Override
            protected void done() {
                try {
                    Boolean success = get();
                    if (Boolean.TRUE.equals(success)) {
                        JOptionPane.showMessageDialog(view,
                                "Contraseña establecida exitosamente.\nYa puede iniciar sesión.",
                                "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        view.clearFields();

                        // ✅ Más simple: la vista ya es un JFrame
                        ((JFrame) SwingUtilities.getRoot(view)).dispose();

                    } else {
                        showError("No se pudo establecer la contraseña. Verifique el ID de usuario.");
                    }
                } catch (Exception ex) {
                    showError("Error al establecer contraseña: " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    view.showLoading(false);
                }
            }
        };
        worker.execute();
    }

    public boolean isValidPassword(String password) {
        if (password == null) return false;
        return password.trim().length() >= 6;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}