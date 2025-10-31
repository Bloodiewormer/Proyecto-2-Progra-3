package org.example.Services;

import org.example.Domain.Dtos.Receta.*;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RecetaService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public RecetaService(String host, int port) {
        super(host, port);
    }

    public Future<RecetaResponseDto> addRecetaAsync(AddRecetaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Receta",
                    "add",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), RecetaResponseDto.class);
        });
    }

    public Future<RecetaResponseDto> updateRecetaAsync(UpdateRecetaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Receta",
                    "update",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), RecetaResponseDto.class);
        });
    }

    public Future<Boolean> deleteRecetaAsync(DeleteRecetaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Receta",
                    "delete",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }

    public Future<List<RecetaResponseDto>> listRecetasAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Receta",
                    "list",
                    "",
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListRecetaResponseDto listResponse = gson.fromJson(response.getData(), ListRecetaResponseDto.class);
            return listResponse.getRecetas();
        });
    }

    public Future<RecetaResponseDto> getRecetaByIdAsync(int id) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Receta",
                    "getById",
                    String.valueOf(id),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), RecetaResponseDto.class);
        });
    }
}