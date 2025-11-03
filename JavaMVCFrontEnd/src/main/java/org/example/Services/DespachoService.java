package org.example.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.Receta.UpdateEstadoRecetaRequestDto;
import org.example.Domain.Dtos.Receta.UpdateRecetaRequestDto;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DespachoService extends BaseService {
    private final Gson gson = new Gson();

    public DespachoService(String host, int port) {
        super(host, port);
    }

    public List<RecetaResponseDto> listarRecetas() {
        RequestDto request = new RequestDto("Despacho", "listarRecetas", "", null);
        ResponseDto response = sendRequest(request);

        if (!response.isSuccess()) {
            System.err.println("[DespachoService] Error: " + response.getMessage());
            return new ArrayList<>();
        }

        Type listType = new TypeToken<List<RecetaResponseDto>>(){}.getType();
        return gson.fromJson(response.getData(), listType);
    }

    public List<RecetaResponseDto> listarPorEstado(String estado) {
        RequestDto request = new RequestDto("Despacho", "listarPorEstado", estado, null);
        ResponseDto response = sendRequest(request);

        if (!response.isSuccess()) {
            System.err.println("[DespachoService] Error: " + response.getMessage());
            return new ArrayList<>();
        }

        Type listType = new TypeToken<List<RecetaResponseDto>>(){}.getType();
        return gson.fromJson(response.getData(), listType);
    }

    public List<RecetaResponseDto> listarPorPaciente(int idPaciente) {
        RequestDto request = new RequestDto("Despacho", "listarPorPaciente", String.valueOf(idPaciente), null);
        ResponseDto response = sendRequest(request);

        if (!response.isSuccess()) {
            System.err.println("[DespachoService] Error: " + response.getMessage());
            return new ArrayList<>();
        }

        Type listType = new TypeToken<List<RecetaResponseDto>>(){}.getType();
        return gson.fromJson(response.getData(), listType);
    }

    // En JavaMVCFrontEnd/src/main/java/org/example/Services/DespachoService.java
    public RecetaResponseDto actualizarEstado(int idReceta, String nuevoEstado) {
        UpdateEstadoRecetaRequestDto dto = new UpdateEstadoRecetaRequestDto(idReceta, nuevoEstado);
        RequestDto request = new RequestDto("Despacho", "actualizarEstado", gson.toJson(dto), null);
        ResponseDto response = sendRequest(request);
        if (!response.isSuccess()) {
            System.err.println("[DespachoService] Error: " + response.getMessage());
            return null;
        }
        return response.getData() != null
                ? gson.fromJson(response.getData(), RecetaResponseDto.class)
                : null;
    }

    public PacienteResponseDto buscarPaciente(int idPaciente) {
        RequestDto request = new RequestDto("Despacho", "buscarPaciente", String.valueOf(idPaciente), null);
        ResponseDto response = sendRequest(request);

        if (!response.isSuccess()) {
            System.err.println("[DespachoService] Error: " + response.getMessage());
            return null;
        }

        return gson.fromJson(response.getData(), PacienteResponseDto.class);
    }
}