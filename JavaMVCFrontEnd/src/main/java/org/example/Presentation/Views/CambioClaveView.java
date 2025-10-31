package org.example.Presentation.Views;

import org.example.Presentation.Components.BlueRoundedButton;
import org.example.Presentation.Components.CustomPasswordField;
import org.example.Presentation.Components.CustomTextField;
import org.example.Presentation.Controllers.CambioClaveController;
import org.example.Presentation.Controllers.LoginController;

import javax.swing.*;
import java.awt.geom.RoundRectangle2D;

public class CambioClaveView extends JFrame{
    private JPanel mainPanel;
    private JTextField idTextField;
    private JPasswordField passwordField;
    private JButton acceptButton;
    @SuppressWarnings("unused")
    private JPanel textPasswordChangePanel;
    @SuppressWarnings("unused")
    private JLabel changePasswordLabel;
    private JPasswordField confirmPasswordField;

    private final CambioClaveController cambioClaveController;


    public CambioClaveView( LoginController controller) {
        this.cambioClaveController = new CambioClaveController(controller);
        initializeFrame();
        initializeEvents();
    }

    private void initializeFrame() {
        setUndecorated(true);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setSize(300, 150);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setVisible(true);
    }

    private void initializeEvents() {
        acceptButton.addActionListener(e -> handleAccept());
    }

    private void handleAccept() {
        String idText = idTextField.getText();
        String newPassword = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (idText.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID de usuario debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!cambioClaveController.isValidUserId(idText) || !cambioClaveController.userExists(id)) {
            JOptionPane.showMessageDialog(this, "El ID de usuario no es válido o no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!cambioClaveController.isValidPassword(newPassword)) {
            JOptionPane.showMessageDialog(this, "La nueva contraseña no cumple con los requisitos de seguridad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cambioClaveController.changePassword(id, newPassword)) {
            JOptionPane.showMessageDialog(this, "Contraseña cambiada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Error al cambiar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void createUIComponents() {
        idTextField = new CustomTextField();
        acceptButton = new BlueRoundedButton("Aceptar");
        passwordField = new CustomPasswordField();
        confirmPasswordField = new CustomPasswordField();
    }
}
