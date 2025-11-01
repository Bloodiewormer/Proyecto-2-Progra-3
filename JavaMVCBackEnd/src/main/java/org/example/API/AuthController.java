package org.example.API;
import com.google.gson.Gson;
import org.example.DataAcces.services.UsuarioService;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.auth.LoginRequestDto;
import org.example.Domain.dtos.auth.UserResponseDto;
import org.example.Domain.models.Usuario;

public class AuthController {
    private final UsuarioService usuarioService;
    private final Gson gson = new Gson();

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public ResponseDto route(RequestDto request) {
        try {
            return switch (request.getRequest()) {
                case "login" -> handleLogin(request);
                default -> new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            };
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

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
                    "", // email (no usado en este sistema)
                    tipoUsuario,
                    usuario.getCreatedAt().toString(),
                    usuario.getUpdatedAt().toString()
            );

            return new ResponseDto(true, "Login successful", gson.toJson(userDto));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDto(false, "Login error: " + e.getMessage(), null);
        }
    }
}
