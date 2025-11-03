package org.example.Domain.Dtos.Auth;

public class ChangePasswordRequestDto {
    private int userId;
    private String currentPassword;
    private String newPassword;

    public ChangePasswordRequestDto() {}

    public ChangePasswordRequestDto(int userId, String currentPassword, String newPassword) {
        this.userId = userId;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}