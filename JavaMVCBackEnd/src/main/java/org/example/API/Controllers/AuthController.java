package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.UsuarioService;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.Auth.LoginRequestDto;
import org.example.Domain.dtos.Auth.RegisterRequestDto;
import org.example.Domain.dtos.Auth.UserResponseDto;
import org.example.Domain.models.Usuario;

public class AuthController {
    private final UsuarioService usuarioService;
    private final Gson gson = new Gson();

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "login":
                    return handleLogin(request);
                case "register":
                    return handleRegister(request);
                case "logout":
                    return handleLogout(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    // --- LOGIN ---
    private ResponseDto handleLogin(RequestDto request) {
        try {
            LoginRequestDto loginDto = gson.fromJson(request.getData(), LoginRequestDto.class);
            Usuario usuario = usuarioService.login(loginDto.getUsernameOrEmail(), loginDto.getPassword());
            if (usuario == null) {
                return new ResponseDto(false, "Invalid credentials", null);
            }
            String tipoUsuario = usuarioService.getTipoUsuario(usuario);
            UserResponseDto userDto = new UserResponseDto(
                    usuario.getId(),
                    usuario.getNombre(),
                    "", // email no usado
                    tipoUsuario,
                    usuario.getCreatedAt().toString(),
                    usuario.getUpdatedAt().toString()
            );
            return new ResponseDto(true, "Login successful", gson.toJson(userDto));
        } catch (Exception e) {
            throw e;
        }
    }

    // --- REGISTER ---
    private ResponseDto handleRegister(RequestDto request) {
        try {
            RegisterRequestDto regDto = gson.fromJson(request.getData(), RegisterRequestDto.class);
            // Aquí debes decidir a qué tipo de usuario registrar (ejemplo: solo médicos)
            Usuario usuario = usuarioService.createMedico(
                    regDto.getUsername(),
                    regDto.getPassword(),
                    regDto.getRole() // Puedes usar este campo como especialidad
            );
            UserResponseDto userDto = new UserResponseDto(
                    usuario.getId(),
                    usuario.getNombre(),
                    "", // email no usado
                    "MEDICO", // o el tipo correspondiente
                    usuario.getCreatedAt().toString(),
                    usuario.getUpdatedAt().toString()
            );
            return new ResponseDto(true, "User registered successfully", gson.toJson(userDto));
        } catch (Exception e) {
            throw e;
        }
    }

    // --- LOGOUT ---
    private ResponseDto handleLogout(RequestDto request) {
        return new ResponseDto(true, "Logout successful", null);
    }

    // --- HELPER: GET USER BY USERNAME ---
    public UserResponseDto getUserByUsername(String username) {
        Usuario usuario = usuarioService.login(username, ""); // Solo para obtener el usuario, ignora clave
        if (usuario == null) return null;
        String tipoUsuario = usuarioService.getTipoUsuario(usuario);
        return new UserResponseDto(
                usuario.getId(),
                usuario.getNombre(),
                "",
                tipoUsuario,
                usuario.getCreatedAt().toString(),
                usuario.getUpdatedAt().toString()
        );
    }
}
