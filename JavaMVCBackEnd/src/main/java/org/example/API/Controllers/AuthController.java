package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.UsuarioService;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.Auth.LoginRequestDto;
import org.example.Domain.dtos.Auth.RegisterRequestDto;
import org.example.Domain.dtos.Auth.UserResponseDto;
import org.example.Domain.models.Usuario;
import org.example.Domain.models.Medico;
import org.example.Domain.models.Farmaceuta;
import org.example.Domain.models.Administrador;

/**
 * Controlador de autenticación que maneja login, registro y logout
 */
public class AuthController {
    private final UsuarioService usuarioService;
    private final Gson gson = new Gson();

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Enruta las peticiones a sus métodos correspondientes
     */
    public ResponseDto route(RequestDto request) {
        try {
            if (request.getRequest() == null) {
                return new ResponseDto(false, "Petición sin tipo de request", null);
            }

            switch (request.getRequest()) {
                case "login":
                    return handleLogin(request);
                case "register":
                    return handleRegister(request);
                case "logout":
                    return handleLogout(request);
                default:
                    return new ResponseDto(false, "Petición desconocida: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            System.err.println("[AuthController] Error en route: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error interno: " + e.getMessage(), null);
        }
    }

    /**
     * Maneja el inicio de sesión
     */
    private ResponseDto handleLogin(RequestDto request) {
        try {
            if (request.getData() == null || request.getData().isEmpty()) {
                return new ResponseDto(false, "Datos de login vacíos", null);
            }

            LoginRequestDto loginDto = gson.fromJson(request.getData(), LoginRequestDto.class);

            if (loginDto.getUsernameOrEmail() == null || loginDto.getPassword() == null) {
                return new ResponseDto(false, "Usuario y contraseña son requeridos", null);
            }

            // Intentar login
            Usuario usuario = usuarioService.login(loginDto.getUsernameOrEmail(), loginDto.getPassword());

            if (usuario == null) {
                return new ResponseDto(false, "Credenciales inválidas", null);
            }

            // Obtener tipo de usuario
            String tipoUsuario = usuarioService.getTipoUsuario(usuario);

            // Construir respuesta
            UserResponseDto userDto = new UserResponseDto(
                    usuario.getId(),
                    usuario.getNombre(),
                    "", // email no usado en este sistema
                    tipoUsuario,
                    usuario.getCreatedAt() != null ? usuario.getCreatedAt().toString() : "",
                    usuario.getUpdatedAt() != null ? usuario.getUpdatedAt().toString() : ""
            );

            System.out.println("[AuthController] Login exitoso para: " + usuario.getNombre() + " (" + tipoUsuario + ")");
            return new ResponseDto(true, "Login exitoso", gson.toJson(userDto));

        } catch (Exception e) {
            System.err.println("[AuthController] Error en handleLogin: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error procesando login: " + e.getMessage(), null);
        }
    }

    /**
     * Maneja el registro de nuevos usuarios
     */
    private ResponseDto handleRegister(RequestDto request) {
        try {
            if (request.getData() == null || request.getData().isEmpty()) {
                return new ResponseDto(false, "Datos de registro vacíos", null);
            }

            RegisterRequestDto regDto = gson.fromJson(request.getData(), RegisterRequestDto.class);

            if (regDto.getUsername() == null || regDto.getPassword() == null) {
                return new ResponseDto(false, "Usuario y contraseña son requeridos", null);
            }

            // Por defecto, registrar como médico
            // Puedes cambiar esto según tu lógica de negocio
            String especialidad = regDto.getRole() != null ? regDto.getRole() : "General";

            Usuario usuario = usuarioService.createMedico(
                    regDto.getUsername(),
                    regDto.getPassword(),
                    especialidad
            );

            if (usuario == null) {
                return new ResponseDto(false, "No se pudo crear el usuario", null);
            }

            String tipoUsuario = usuarioService.getTipoUsuario(usuario);

            UserResponseDto userDto = new UserResponseDto(
                    usuario.getId(),
                    usuario.getNombre(),
                    regDto.getEmail() != null ? regDto.getEmail() : "",
                    tipoUsuario,
                    usuario.getCreatedAt() != null ? usuario.getCreatedAt().toString() : "",
                    usuario.getUpdatedAt() != null ? usuario.getUpdatedAt().toString() : ""
            );

            System.out.println("[AuthController] Usuario registrado: " + usuario.getNombre());
            return new ResponseDto(true, "Usuario registrado exitosamente", gson.toJson(userDto));

        } catch (Exception e) {
            System.err.println("[AuthController] Error en handleRegister: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error procesando registro: " + e.getMessage(), null);
        }
    }

    /**
     * Maneja el cierre de sesión
     */
    private ResponseDto handleLogout(RequestDto request) {
        try {
            System.out.println("[AuthController] Logout procesado");
            return new ResponseDto(true, "Logout exitoso", null);
        } catch (Exception e) {
            System.err.println("[AuthController] Error en handleLogout: " + e.getMessage());
            return new ResponseDto(false, "Error procesando logout: " + e.getMessage(), null);
        }
    }

    /**
     * Obtiene un usuario por nombre de usuario (método helper)
     */
    public UserResponseDto getUserByUsername(String username) {
        try {
            // Primero intentar login con credenciales vacías solo para obtener el usuario
            // En un sistema real, deberías tener un método específico para esto
            Usuario usuario = usuarioService.getMedicoById(1L); // Esto es temporal

            if (usuario == null) {
                return null;
            }

            String tipoUsuario = usuarioService.getTipoUsuario(usuario);

            return new UserResponseDto(
                    usuario.getId(),
                    usuario.getNombre(),
                    "",
                    tipoUsuario,
                    usuario.getCreatedAt() != null ? usuario.getCreatedAt().toString() : "",
                    usuario.getUpdatedAt() != null ? usuario.getUpdatedAt().toString() : ""
            );
        } catch (Exception e) {
            System.err.println("[AuthController] Error en getUserByUsername: " + e.getMessage());
            return null;
        }
    }
}