package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.CustomButton;
import org.example.presentation_layer.Components.CustomPaswordField;
import org.example.presentation_layer.Components.CustomTextField;

import javax.swing.*;

public class LoginView {
    private JPanel MainPanel;
    private JLabel AppIcon;
    private JPanel IconPanel;
    private JPanel LoginPanel;
    private JTextField UserIDField;
    private JPasswordField passwordField;
    private JButton LoginButton;
    private JButton RegistraseButton;
    private JLabel ForgotLable;

    //put image in the image panel


    public JPanel getMainPanel() {
        return MainPanel;
    }


    // Java
    private void createUIComponents() {
        LoginButton = new CustomButton("LOGIN");
        UserIDField = new CustomTextField();
        RegistraseButton = new CustomButton("REGISTER");
        passwordField = new CustomPaswordField();

    }
}
