package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.BlueRoundedButton;
import org.example.presentation_layer.Components.CustomPaswordField;
import org.example.presentation_layer.Components.CustomTextField;
import org.example.presentation_layer.Controllers.LoginController;
import org.example.presentation_layer.Models.UserType;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginView  extends  JFrame {
    private JPanel MainPanel;
    private JTextField UserIDField;
    private JPasswordField passwordField;
    private JButton LoginButton;
    private JButton RegistraseButton;
    private JLabel ForgotLable;
    private JLabel CloseProgram;
    private JPanel LoginPanel;
    private JPanel IconPanel;
    private JLabel Icon;
    private JLabel LoginLable;
    private JLabel info;

    private final LoginController controller;


    public LoginView(LoginController controller) {
        this.controller = controller;
        setContentPane(MainPanel);
        setTitle("Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
        setVisible(true);
        setLocationRelativeTo(null);


        RegistraseButton.addActionListener( e -> onRegisterClicked());
        LoginButton.addActionListener(e -> onLoginClicked());

        ForgotLable.addComponentListener(new ComponentAdapter() {
        });
        ForgotLable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JOptionPane.showMessageDialog(null, "Password recovery is not implemented yet.");
            }
        });
        CloseProgram.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.exit(0);
            }
        });
        info.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //menu con los nombres de los integrantes del grupo

                JOptionPane.showMessageDialog(null,
                        "Group Members:\n" +
                                "- David Gonzalez Cordoba\n" +
                                "- Dany Montero Romero\n" +
                                "- Emmanuel Nunez Jimenez", "Integrantes del grupo", JOptionPane.INFORMATION_MESSAGE);

            }
        });
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void createUIComponents() {
        LoginButton = new BlueRoundedButton("LOGIN");
        UserIDField = new CustomTextField();
        RegistraseButton = new BlueRoundedButton("REGISTER");
        passwordField = new CustomPaswordField();
    }

    private void onLoginClicked() {
        String userInput = UserIDField.getText().trim();
        String password = new String(passwordField.getPassword());
        int id;

        try {
            id = Integer.parseInt(userInput);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
            return;
        }
        boolean success = controller.login(id, password);

        if (success) {
            UserType userType = controller.getUserType(id);
            // Delegate navigation to controller
            controller.onLoginSuccess(userType, this);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.");
        }

    }

    private void onRegisterClicked() {
        // Crear y mostrar la nueva ventana
        //JFrame registerWindow = new RegisterView();
        //registerWindow.setVisible(true);
        JOptionPane.showMessageDialog(null, "Opcion de registro no implementada.");

    }

    public void showLoginView() {
        new LoginView(this.controller);
    }








}


// src/main/java/org/example/presentation_layer/Views/LoginView.java


