package org.example;

import javax.swing.*;

//azul usado para botones #004aad

public class Main {
    public static void main(String[] args) {

        //call login view
        org.example.presentation_layer.Views.LoginView loginView = new org.example.presentation_layer.Views.LoginView ();
        JFrame frame = new JFrame("Login");
        frame.setContentPane(loginView.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);



    }
}