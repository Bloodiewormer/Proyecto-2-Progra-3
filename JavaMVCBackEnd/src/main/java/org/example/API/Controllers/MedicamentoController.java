package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.MedicamentoService;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.Dtos.Medicamento.*;
import org.example.Domain.models.Medicamento;

import java.util.List;
import java.util.stream.Collectors;

public class MedicamentoController {
    private final MedicamentoService medicamentoService;
    private final Gson gson = new Gson();

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
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
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    private ResponseDto handleAdd(RequestDto request) {
        try {
            AddMedicamentoRequestDto dto = gson.fromJson(request.getData(), AddMedicamentoRequestDto.class);
            Medicamento medicamento = medicamentoService.create(dto.getNombre(), dto.getPresentacion());

            MedicamentoResponseDto response = toResponseDto(medicamento);
            return new ResponseDto(true, "Medicamento agregado exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicamentoController] Error en handleAdd: " + e.getMessage());
            return new ResponseDto(false, "Error agregando medicamento: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleUpdate(RequestDto request) {
        try {
            UpdateMedicamentoRequestDto dto = gson.fromJson(request.getData(), UpdateMedicamentoRequestDto.class);
            Medicamento updated = medicamentoService.update(
                    Long.valueOf(dto.getId()),
                    dto.getNombre(),
                    dto.getPresentacion()
            );

            if (updated == null) {
                return new ResponseDto(false, "Medicamento no encontrado", null);
            }

            MedicamentoResponseDto response = toResponseDto(updated);
            return new ResponseDto(true, "Medicamento actualizado exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicamentoController] Error en handleUpdate: " + e.getMessage());
            return new ResponseDto(false, "Error actualizando medicamento: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleDelete(RequestDto request) {
        try {
            DeleteMedicamentoRequestDto dto = gson.fromJson(request.getData(), DeleteMedicamentoRequestDto.class);
            boolean deleted = medicamentoService.delete(Long.valueOf(dto.getId()));

            if (!deleted) {
                return new ResponseDto(false, "Medicamento no encontrado", null);
            }

            return new ResponseDto(true, "Medicamento eliminado exitosamente", null);
        } catch (Exception e) {
            System.err.println("[MedicamentoController] Error en handleDelete: " + e.getMessage());
            return new ResponseDto(false, "Error eliminando medicamento: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleList(RequestDto request) {
        try {
            List<Medicamento> medicamentos = medicamentoService.getAll();
            List<MedicamentoResponseDto> medicamentoDtos = medicamentos.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListMedicamentoResponseDto response = new ListMedicamentoResponseDto(medicamentoDtos);
            return new ResponseDto(true, "Medicamentos obtenidos exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicamentoController] Error en handleList: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo medicamentos: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleGet(RequestDto request) {
        try {
            DeleteMedicamentoRequestDto dto = gson.fromJson(request.getData(), DeleteMedicamentoRequestDto.class);
            Medicamento medicamento = medicamentoService.getById(Long.valueOf(dto.getId()));

            if (medicamento == null) {
                return new ResponseDto(false, "Medicamento no encontrado", null);
            }

            MedicamentoResponseDto response = toResponseDto(medicamento);
            return new ResponseDto(true, "Medicamento obtenido exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicamentoController] Error en handleGet: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo medicamento: " + e.getMessage(), null);
        }
    }

    private MedicamentoResponseDto toResponseDto(Medicamento medicamento) {
        return new MedicamentoResponseDto(
                medicamento.getId().intValue(),
                medicamento.getNombre(),
                medicamento.getPresentacion()
        );
    }
}