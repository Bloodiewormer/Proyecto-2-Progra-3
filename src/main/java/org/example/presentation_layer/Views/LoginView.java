package org.example.presentation_layer.Views;

import org.example.presentation_layer.Components.CustomButton;
import org.example.presentation_layer.Components.CustomPaswordField;
import org.example.presentation_layer.Components.CustomTextField;

import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginView  extends  JFrame {
    private JPanel MainPanel;
    private JLabel AppIcon;
    private JPanel IconPanel;
    private JPanel LoginPanel;
    private JTextField UserIDField;
    private JPasswordField passwordField;
    private JButton LoginButton;
    private JButton RegistraseButton;
    private JLabel ForgotLable;
    private JLabel CloseProgram;


    public LoginView() {
        setContentPane(MainPanel);
        setTitle("Login");
        setSize(740, 400);
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
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    private void createUIComponents() {
        LoginButton = new CustomButton("LOGIN");
        UserIDField = new CustomTextField();
        RegistraseButton = new CustomButton("REGISTER");
        passwordField = new CustomPaswordField();

    }

    private void onLoginClicked() {
        String userInput = UserIDField.getText().trim();
        String password = new String(passwordField.getPassword());
        JOptionPane.showMessageDialog(null, "opcion de login no implementada.");

    }

    private void onRegisterClicked() {
        // Crear y mostrar la nueva ventana
        //JFrame registerWindow = new RegisterView();
        //registerWindow.setVisible(true);
        JOptionPane.showMessageDialog(null, "Opcion de registro no implementada.");

    }








}
