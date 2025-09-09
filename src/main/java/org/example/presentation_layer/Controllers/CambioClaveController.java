package org.example.presentation_layer.Controllers;

public class CambioClaveController {

    LoginController loginController;

    public CambioClaveController(LoginController loginController) {
        this.loginController = loginController;

    }

    public boolean IsValidPassword(String newPassword) {
        if (newPassword != null && newPassword.length() >= 8
                && newPassword.matches(".*[A-Z].*")
                && newPassword.matches(".*[a-z].*")
                && newPassword.matches(".*[0-9].*")
                && newPassword.matches(".*[!@#$%^&*()].*")
                && !newPassword.contains(" ")) {
            return true;
        }
        return false;
    }

    public boolean IsValidUserID(String userID) {
        try {
            int id = Integer.parseInt(userID);
            return id > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean usuarioExiste(int id) {
        return loginController.getUserType(id) != null;
    }

    public boolean cambiarClave(int id, String newPassword) {
            return loginController.changePassword(id, newPassword);
    }

}
