package org.example.Services;

import org.example.Domain.Dtos.Receta.AddRecetaRequestDto;
import org.example.Domain.Dtos.Receta.DeleteRecetaRequestDto;
import org.example.Domain.Dtos.Receta.ListRecetaResponseDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.Receta.UpdateRecetaRequestDto;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

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

    public Future<ListRecetaResponseDto> listRecetasAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Receta",
                    "list",
                    null,
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), ListRecetaResponseDto.class);
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

    public Future<ListRecetaResponseDto> listRecetasByPacienteAsync(int pacienteId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Receta",
                    "listByPaciente",
                    String.valueOf(pacienteId),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), ListRecetaResponseDto.class);
        });
    }

    public Future<ListRecetaResponseDto> listRecetasByMedicoAsync(int medicoId) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Receta",
                    "listByMedico",
                    String.valueOf(medicoId),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), ListRecetaResponseDto.class);
        });
    }
}
