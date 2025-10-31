package org.example.Services;

import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.Dtos.Receta.AddRecetaRequestDto;
import org.example.Domain.Dtos.Receta.DeleteRecetaRequestDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.Receta.ListRecetaResponseDto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RecetaService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public RecetaService(String host, int port) {
        super(host, port);
    }

    public Future<RecetaResponseDto> addRecetaAsync(AddRecetaRequestDto dto, String userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "add", gson.toJson(dto), userId);
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), RecetaResponseDto.class);
        });
    }

    public Future<Boolean> deleteRecetaAsync(DeleteRecetaRequestDto dto, String userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "delete", gson.toJson(dto), userId);
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }

    public Future<List<RecetaResponseDto>> listRecetasAsync(String userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "list", "", userId);
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListRecetaResponseDto listResponse = gson.fromJson(response.getData(), ListRecetaResponseDto.class);
            return listResponse.getRecetas();
        });
    }

    public Future<List<RecetaResponseDto>> listRecetasByPacienteAsync(int idPaciente, String userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "listByPaciente", String.valueOf(idPaciente), userId);
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListRecetaResponseDto listResponse = gson.fromJson(response.getData(), ListRecetaResponseDto.class);
            return listResponse.getRecetas();
        });
    }

    public Future<RecetaResponseDto> getRecetaByIdAsync(int id, String userId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto("Receta", "getById", String.valueOf(id), userId);
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), RecetaResponseDto.class);
        });
    }
}