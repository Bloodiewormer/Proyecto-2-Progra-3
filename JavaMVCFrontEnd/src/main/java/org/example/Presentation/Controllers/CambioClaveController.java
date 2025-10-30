package org.example.Presentation.Controllers;

public class CambioClaveController {
    private final LoginController loginController;

    public CambioClaveController(LoginController loginController) {
        this.loginController = loginController;
    }

    public boolean isValidPassword(String newPassword) {
        return newPassword != null && newPassword.length() >= 8
                && newPassword.matches(".*[A-Z].*")
                && newPassword.matches(".*[a-z].*")
                && newPassword.matches(".*[0-9].*")
                && newPassword.matches(".*[!@#$%^&*()].*")
                && !newPassword.contains(" ");
    }

    public boolean isValidUserId(String userID) {
        try {
            int id = Integer.parseInt(userID);
            return id > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean userExists(int id) {
        return loginController.getUserType(id) != null;
    }

    public boolean changePassword(int id, String newPassword) {
        return loginController.changePassword(id, newPassword);
    }
}

