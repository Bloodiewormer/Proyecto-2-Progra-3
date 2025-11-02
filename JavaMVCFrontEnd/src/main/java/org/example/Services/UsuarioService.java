package org.example.Services;

import org.example.Domain.Dtos.Farmaceuta.*;
import org.example.Domain.Dtos.Medico.*;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UsuarioService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public UsuarioService(String host, int port) {
        super(host, port);
    }

    // ==================== FARMACEUTA ====================

    public Future<FarmaceutaResponseDto> addFarmaceutaAsync(AddFarmaceutaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Farmaceuta",
                    "agregar",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), FarmaceutaResponseDto.class);
        });
    }

    public Future<FarmaceutaResponseDto> updateFarmaceutaAsync(UpdateFarmaceutaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Farmaceuta",
                    "update",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), FarmaceutaResponseDto.class);
        });
    }

    public Future<Boolean> deleteFarmaceutaAsync(DeleteFarmaceutaRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Farmaceuta",
                    "delete",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }

    public Future<List<FarmaceutaResponseDto>> listFarmaceutasAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Farmaceuta",
                    "listar",   // <-- antes decía "list"
                    "",
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListFarmaceutaResponseDto listResponse = gson.fromJson(response.getData(), ListFarmaceutaResponseDto.class);
            return listResponse.getFarmaceutas();
        });
    }

    // ==================== MEDICO ====================

    public Future<MedicoResponseDto> addMedicoAsync(AddMedicoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medico",
                    "add",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MedicoResponseDto.class);
        });
    }

    public Future<MedicoResponseDto> updateMedicoAsync(UpdateMedicoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medico",
                    "update",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MedicoResponseDto.class);
        });
    }

    public Future<Boolean> deleteMedicoAsync(DeleteMedicoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medico",
                    "delete",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }

    public Future<List<MedicoResponseDto>> listMedicosAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medico",
                    "list",
                    "",
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListMedicoResponseDto listResponse = gson.fromJson(response.getData(), ListMedicoResponseDto.class);
            return listResponse.getMedicos();
        });
    }
}