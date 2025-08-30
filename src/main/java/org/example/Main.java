package org.example;

import org.example.presentation_layer.Views.LoginView;

import javax.swing.*;

//azul usado para botones #004aad

public class Main {
    public static void main(String[] args) {



        //call login view
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
        });






    }
}