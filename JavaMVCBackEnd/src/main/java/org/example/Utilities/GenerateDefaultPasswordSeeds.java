package org.example.Utilities;

public class GenerateDefaultPasswordSeeds {
    public static void main(String[] args) {
        String defaultPassword = "temp123"; // Contraseña por defecto

        String[][] users = {
                {"admin", "admin123", "TRUE"},  // Admin con contraseña conocida
                {"dr_garcia", defaultPassword, "FALSE"},
                {"dra_martinez", defaultPassword, "FALSE"},
                {"dr_lopez", defaultPassword, "FALSE"},
                {"farm_rodriguez", defaultPassword, "FALSE"},
                {"farm_hernandez", defaultPassword, "FALSE"}
        };

        System.out.println("-- Usuarios con contraseñas generadas");
        for (String[] user : users) {
            String salt = PasswordUtils.generateSalt();
            String hash = PasswordUtils.hashPassword(user[1], salt);
            System.out.printf("    ('%s', '%s', '%s', %s),\n",
                    user[0], hash, salt, user[2]);
        }
    }
}