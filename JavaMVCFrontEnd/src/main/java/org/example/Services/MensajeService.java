package org.example.Services;

import org.example.Domain.Dtos.Mensaje.*;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MensajeService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public MensajeService(String host, int port) {
        super(host, port);
    }

    public Future<MensajeResponseDto> addMensajeAsync(AddMensajeRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Mensaje",
                    "add",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MensajeResponseDto.class);
        });
    }

    public Future<ListMensajeResponseDto> listMensajesAsync(String usuario1, String usuario2) {
        return executor.submit(() -> {
            ListMensajeResponseDto dto = new ListMensajeResponseDto(usuario1, usuario2);
            RequestDto request = new RequestDto(
                    "Mensaje",
                    "list",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), ListMensajeResponseDto.class);
        });
    }

    public Future<MensajeResponseDto> updateMensajeAsync(UpdateMensajeRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Mensaje",
                    "update",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), MensajeResponseDto.class);
        });
    }

    public Future<Boolean> deleteMensajeAsync(DeleteMensajeRequestDto dto) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Mensaje",
                    "delete",
                    gson.toJson(dto),
                    null
            );
            ResponseDto response = sendRequest(request);
            return response.isSuccess();
        });
    }
}

