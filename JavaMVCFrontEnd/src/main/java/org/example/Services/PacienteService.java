package org.example.Services;

import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PacienteService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public PacienteService(String host, int port) {
        super(host, port);
    }

    public Future<PacienteResponseDto> addPacienteAsync(AddPacienteRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Paciente",
                    "add",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), PacienteResponseDto.class);
        });
    }

    public Future<PacienteResponseDto> updatePacienteAsync(UpdatePacienteRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Paciente",
                    "update",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), PacienteResponseDto.class);
        });
    }

    public Future<Boolean> deletePacienteAsync(DeletePacienteRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Paciente",
                    "delete",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }

    public Future<List<PacienteResponseDto>> listPacientesAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Paciente",
                    "list",
                    "",
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListPacienteResponseDto listResponse = gson.fromJson(response.getData(), ListPacienteResponseDto.class);
            return listResponse.getPacientes();
        });
    }
}