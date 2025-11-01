package org.example.Services;

import org.example.Domain.Dtos.Auth.LoginRequestDto;
import org.example.Domain.Dtos.Auth.UserResponseDto;
import org.example.Domain.Dtos.Auth.RegisterRequestDto;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AuthService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public AuthService(String host, int port) {
        super(host, port);
    }

    public Future<UserResponseDto> login(String usernameOrEmail, String password) {
        return executor.submit(() -> {
            LoginRequestDto loginDto = new LoginRequestDto(usernameOrEmail, password);
            RequestDto request = new RequestDto(
                    "Auth",
                    "login",
                    gson.toJson(loginDto),
                    null
            );

            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) {
                return null;
            }

            return gson.fromJson(response.getData(), UserResponseDto.class);
        });
    }

    public Future<UserResponseDto> register(String username, String email, String password, String role) {
        return executor.submit(() -> {
            RegisterRequestDto registerDto = new RegisterRequestDto(username, email, password, role);
            RequestDto request = new RequestDto(
                    "Auth",
                    "register",
                    gson.toJson(registerDto),
                    null
            );

            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) {
                return null;
            }

            return gson.fromJson(response.getData(), UserResponseDto.class);
        });
    }

    public Future<Boolean> changePassword(int userId, String currentPassword, String newPassword) {
        return executor.submit(() -> {
            // Implementar según tu backend
            RequestDto request = new RequestDto(
                    "Auth",
                    "changePassword",
                    gson.toJson(new ChangePasswordRequest(userId, currentPassword, newPassword)),
                    null
            );

            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }

    // DTO interno para cambio de contraseña
    private static class ChangePasswordRequest {
        private final int userId;
        private final String currentPassword;
        private final String newPassword;

        public ChangePasswordRequest(int userId, String currentPassword, String newPassword) {
            this.userId = userId;
            this.currentPassword = currentPassword;
            this.newPassword = newPassword;
        }

        public int getUserId() { return userId; }
        public String getCurrentPassword() { return currentPassword; }
        public String getNewPassword() { return newPassword; }
    }
}