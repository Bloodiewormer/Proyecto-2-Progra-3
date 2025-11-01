package org.example.API;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.Farmaceuta.AddFarmaceutaRequestDto;
import org.example.Domain.dtos.Farmaceuta.DeleteFarmaceutaRequestDto;
import org.example.Domain.dtos.Farmaceuta.FarmaceutaResponseDto;
import org.example.Domain.dtos.Farmaceuta.UpdateFarmaceutaRequestDto;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class FarmaceutaController {

    private final List<FarmaceutaResponseDto> farmaceutas;
    private final Gson gson = new Gson();

    public FarmaceutaController(List<FarmaceutaResponseDto> farmaceutas) {
        this.farmaceutas = farmaceutas != null ? farmaceutas : new ArrayList<>();
    }

    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "listar":
                    return handleListar();
                case "agregar":
                    return handleAgregar(request);
                case "actualizar":
                    return handleActualizar(request);
                case "eliminar":
                    return handleEliminar(request);
                case "buscarPorNombre":
                    return handleBuscarPorNombre(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    private ResponseDto handleListar() {
        return new ResponseDto(true, "Farmaceutas obtenidos", gson.toJson(farmaceutas));
    }

    private ResponseDto handleAgregar(RequestDto request) {
        AddFarmaceutaRequestDto dto = gson.fromJson(request.getData(), AddFarmaceutaRequestDto.class);
        FarmaceutaResponseDto nuevo = new FarmaceutaResponseDto(dto.getId(), dto.getNombre());
        farmaceutas.add(nuevo);
        return new ResponseDto(true, "Farmaceuta agregado", gson.toJson(nuevo));
    }

    private ResponseDto handleActualizar(RequestDto request) {
        UpdateFarmaceutaRequestDto dto = gson.fromJson(request.getData(), UpdateFarmaceutaRequestDto.class);
        for (FarmaceutaResponseDto f : farmaceutas) {
            if (f.getId() == dto.getId()) {
                f.setNombre(dto.getNombre());
                return new ResponseDto(true, "Farmaceuta actualizado", gson.toJson(f));
            }
        }
        return new ResponseDto(false, "No se encontró el farmaceuta", null);
    }

    private ResponseDto handleEliminar(RequestDto request) {
        DeleteFarmaceutaRequestDto dto = gson.fromJson(request.getData(), DeleteFarmaceutaRequestDto.class);
        boolean eliminado = farmaceutas.removeIf(f -> f.getId() == dto.getId());
        return new ResponseDto(eliminado, eliminado ? "Farmaceuta eliminado" : "No se encontró el farmaceuta", null);
    }

    private ResponseDto handleBuscarPorNombre(RequestDto request) {
        Type mapType = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> params = gson.fromJson(request.getData(), mapType);
        String nombre = params.get("nombre").toLowerCase();
        List<FarmaceutaResponseDto> filtrados = farmaceutas.stream()
                .filter(f -> f.getNombre().toLowerCase().contains(nombre))
                .collect(Collectors.toList());
        return new ResponseDto(true, "Farmaceutas filtrados", gson.toJson(filtrados));
    }
}
