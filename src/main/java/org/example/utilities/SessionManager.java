package org.example.utilities;

import org.example.domain_layer.Usuario;

public class SessionManager {

    private static Usuario currentUser;

    private SessionManager() {
        // evitar instanciación
    }

    public static void setCurrentUser(Usuario user) {
        currentUser = user;
    }

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }
}
