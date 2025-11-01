package org.example.API;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Domain.dtos.RequestDto;
import org.example.Domain.dtos.ResponseDto;
import org.example.Domain.dtos.Receta.RecetaResponseDto;
import org.example.Domain.dtos.Receta.UpdateRecetaRequestDto;
import org.example.Utilities.EstadoReceta;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class DespachoController {

    private final List<RecetaResponseDto> recetas;
    private final Gson gson = new Gson();

    public DespachoController(List<RecetaResponseDto> recetas) {
        this.recetas = recetas != null ? recetas : new ArrayList<>();
    }

    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "todas":
                    return handleObtenerRecetas();
                case "porEstado":
                    return handleObtenerRecetasPorEstado(request);
                case "porPaciente":
                    return handleObtenerRecetasPorPaciente(request);
                case "actualizarEstado":
                    return handleActualizarEstadoReceta(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    private ResponseDto handleObtenerRecetas() {
        return new ResponseDto(true, "Recetas obtenidas", gson.toJson(recetas));
    }

    private ResponseDto handleObtenerRecetasPorEstado(RequestDto request) {
        Map<String, String> params = gson.fromJson(request.getData(), Map.class);
        String estado = params.get("estado");
        List<RecetaResponseDto> filtradas = recetas.stream()
                .filter(r -> estado.equals(r.getEstado()))
                .collect(Collectors.toList());
        return new ResponseDto(true, "Recetas filtradas por estado", gson.toJson(filtradas));
    }

    private ResponseDto handleObtenerRecetasPorPaciente(RequestDto request) {
        Map<String, Object> params = gson.fromJson(request.getData(), Map.class);
        int idPaciente = ((Number) params.get("idPaciente")).intValue();
        List<RecetaResponseDto> filtradas = recetas.stream()
                .filter(r -> r.getIdPaciente() == idPaciente)
                .collect(Collectors.toList());
        return new ResponseDto(true, "Recetas filtradas por paciente", gson.toJson(filtradas));
    }

    private ResponseDto handleActualizarEstadoReceta(RequestDto request) {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> params = gson.fromJson(request.getData(), type);
        int recetaId = ((Number) params.get("recetaId")).intValue();
        String nuevoEstado = (String) params.get("nuevoEstado");

        RecetaResponseDto receta = recetas.stream()
                .filter(r -> r.getId() == recetaId)
                .findFirst()
                .orElse(null);

        if (receta != null) {
            receta.setEstado(nuevoEstado);
            return new ResponseDto(true, "Estado actualizado correctamente.", null);
        } else {
            return new ResponseDto(false, "No se encontró la receta.", null);
        }
    }
}
