package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.RecetaService;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.Dtos.Receta.*;
import org.example.Domain.Dtos.DetalleReceta.*;
import org.example.Domain.models.Receta;
import org.example.Domain.models.DetalleReceta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RecetaController {
    private final RecetaService recetaService;
    private final Gson gson = new Gson();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public RecetaController(RecetaService recetaService) {
        this.recetaService = recetaService;
    }

    public ResponseDto route(RequestDto request) {
        try {
            switch (request.getRequest()) {
                case "add":
                    return handleAdd(request);
                case "update":
                    return handleUpdate(request);
                case "delete":
                    return handleDelete(request);
                case "list":
                    return handleList(request);
                case "get":
                    return handleGet(request);
                case "porPaciente":
                    return handlePorPaciente(request);
                case "porMedico":
                    return handlePorMedico(request);
                case "porEstado":
                    return handlePorEstado(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    private ResponseDto handleAdd(RequestDto request) {
        try {
            AddRecetaRequestDto dto = gson.fromJson(request.getData(), AddRecetaRequestDto.class);
            LocalDateTime fechaRetiro = LocalDateTime.parse(dto.getFechaConfeccion(), FORMATTER);

            // Crear la receta
            Receta receta = recetaService.create(
                    Long.valueOf(dto.getIdPaciente()),
                    Long.valueOf(dto.getIdMedico()),
                    fechaRetiro
            );

            if (receta == null) {
                return new ResponseDto(false, "No se pudo crear la receta", null);
            }

            // Agregar detalles si existen
            if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
                for (AddDetalleRecetaRequestDto detalle : dto.getDetalles()) {
                    recetaService.addDetalle(
                            receta.getId(),
                            Long.valueOf(detalle.getIdMedicamento()),
                            detalle.getCantidad(),
                            detalle.getIndicaciones(),
                            detalle.getDias()
                    );
                }
            }

            // Obtener la receta completa con detalles
            receta = recetaService.getById(receta.getId());
            RecetaResponseDto response = toResponseDto(receta);
            return new ResponseDto(true, "Receta agregada exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[RecetaController] Error en handleAdd: " + e.getMessage());
            e.printStackTrace();
            return new ResponseDto(false, "Error agregando receta: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleUpdate(RequestDto request) {
        try {
            UpdateRecetaRequestDto dto = gson.fromJson(request.getData(), UpdateRecetaRequestDto.class);

            // Para actualizar, primero obtenemos la receta existente
            Receta receta = recetaService.getById(Long.valueOf(dto.getId()));
            if (receta == null) {
                return new ResponseDto(false, "Receta no encontrada", null);
            }

            // Actualizar detalles si es necesario
            // (Esto es una implementación simplificada)

            RecetaResponseDto response = toResponseDto(receta);
            return new ResponseDto(true, "Receta actualizada exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[RecetaController] Error en handleUpdate: " + e.getMessage());
            return new ResponseDto(false, "Error actualizando receta: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleDelete(RequestDto request) {
        try {
            DeleteRecetaRequestDto dto = gson.fromJson(request.getData(), DeleteRecetaRequestDto.class);
            boolean deleted = recetaService.delete(Long.valueOf(dto.getId()));

            if (!deleted) {
                return new ResponseDto(false, "Receta no encontrada", null);
            }

            return new ResponseDto(true, "Receta eliminada exitosamente", null);
        } catch (Exception e) {
            System.err.println("[RecetaController] Error en handleDelete: " + e.getMessage());
            return new ResponseDto(false, "Error eliminando receta: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleList(RequestDto request) {
        try {
            List<Receta> recetas = recetaService.getAll();
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListRecetaResponseDto response = new ListRecetaResponseDto(recetaDtos);
            return new ResponseDto(true, "Recetas obtenidas exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[RecetaController] Error en handleList: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo recetas: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleGet(RequestDto request) {
        try {
            DeleteRecetaRequestDto dto = gson.fromJson(request.getData(), DeleteRecetaRequestDto.class);
            Receta receta = recetaService.getById(Long.valueOf(dto.getId()));

            if (receta == null) {
                return new ResponseDto(false, "Receta no encontrada", null);
            }

            RecetaResponseDto response = toResponseDto(receta);
            return new ResponseDto(true, "Receta obtenida exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[RecetaController] Error en handleGet: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo receta: " + e.getMessage(), null);
        }
    }

    private ResponseDto handlePorPaciente(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            int idPaciente = ((Number) params.get("idPaciente")).intValue();

            List<Receta> recetas = recetaService.getByPaciente(Long.valueOf(idPaciente));
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListRecetaResponseDto response = new ListRecetaResponseDto(recetaDtos);
            return new ResponseDto(true, "Recetas obtenidas exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[RecetaController] Error en handlePorPaciente: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo recetas: " + e.getMessage(), null);
        }
    }

    private ResponseDto handlePorMedico(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            int idMedico = ((Number) params.get("idMedico")).intValue();

            List<Receta> recetas = recetaService.getByMedico(Long.valueOf(idMedico));
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListRecetaResponseDto response = new ListRecetaResponseDto(recetaDtos);
            return new ResponseDto(true, "Recetas obtenidas exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[RecetaController] Error en handlePorMedico: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo recetas: " + e.getMessage(), null);
        }
    }

    private ResponseDto handlePorEstado(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            String estado = (String) params.get("estado");

            List<Receta> recetas = recetaService.getByEstado(
                    org.example.Utilities.EstadoReceta.valueOf(estado)
            );
            List<RecetaResponseDto> recetaDtos = recetas.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListRecetaResponseDto response = new ListRecetaResponseDto(recetaDtos);
            return new ResponseDto(true, "Recetas obtenidas exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[RecetaController] Error en handlePorEstado: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo recetas: " + e.getMessage(), null);
        }
    }

    private RecetaResponseDto toResponseDto(Receta receta) {
        List<DetalleRecetaResponseDto> detalles = receta.getDetalles().stream()
                .map(this::toDetalleResponseDto)
                .collect(Collectors.toList());

        return new RecetaResponseDto(
                receta.getId().intValue(),
                receta.getPaciente().getId().intValue(),
                receta.getMedico().getId().intValue(),
                receta.getFechaConfeccion().format(FORMATTER),
                detalles,
                receta.getEstado().toString()
        );
    }

    private DetalleRecetaResponseDto toDetalleResponseDto(DetalleReceta detalle) {
        return new DetalleRecetaResponseDto(
                detalle.getId().intValue(),
                detalle.getMedicamento().getId().intValue(),
                detalle.getCantidad(),
                detalle.getIndicaciones(),
                detalle.getDias()
        );
    }
}