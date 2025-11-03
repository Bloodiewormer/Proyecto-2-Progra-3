package org.example.Domain.Dtos.Auth;

public class LoginRequestDto {
    private int userId;
    private String password;

    public LoginRequestDto() {}

    public LoginRequestDto(int userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}