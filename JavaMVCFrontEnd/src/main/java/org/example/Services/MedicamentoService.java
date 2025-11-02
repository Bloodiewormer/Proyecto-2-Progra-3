package org.example.Services;

import org.example.Domain.Dtos.Medicamento.*;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MedicamentoService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public MedicamentoService(String host, int port) {
        super(host, port);
    }

    public Future<MedicamentoResponseDto> addMedicamentoAsync(AddMedicamentoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medicamento",
                    "add",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MedicamentoResponseDto.class);
        });
    }

    public Future<MedicamentoResponseDto> updateMedicamentoAsync(UpdateMedicamentoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medicamento",
                    "update",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MedicamentoResponseDto.class);
        });
    }

    public Future<Boolean> deleteMedicamentoAsync(DeleteMedicamentoRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medicamento",
                    "delete",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }

    public Future<List<MedicamentoResponseDto>> listMedicamentosAsync() {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medicamento",
                    "list",
                    "",
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            ListMedicamentoResponseDto listResponse = gson.fromJson(response.getData(), ListMedicamentoResponseDto.class);
            return listResponse.getMedicamentos();
        });
    }
    public Future<MedicamentoResponseDto> getMedicamentoByIdAsync(int id) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Medicamento",
                    "getById",
                    String.valueOf(id),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MedicamentoResponseDto.class);
        });
    }

}