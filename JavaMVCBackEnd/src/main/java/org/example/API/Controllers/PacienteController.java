package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.PacienteService;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.Dtos.Paciente.*;
import org.example.Domain.models.Paciente;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PacienteController {
    private final PacienteService pacienteService;
    private final Gson gson = new Gson();

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
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
                case "buscarPorNombre":
                    return handleBuscarPorNombre(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    private ResponseDto handleAdd(RequestDto request) {
        try {
            AddPacienteRequestDto dto = gson.fromJson(request.getData(), AddPacienteRequestDto.class);
            LocalDate fechaNacimiento = LocalDate.parse(dto.getFechaNacimiento());

            Paciente paciente = pacienteService.create(
                    dto.getNombre(),
                    dto.getTelefono(),
                    fechaNacimiento
            );

            PacienteResponseDto response = toResponseDto(paciente);
            return new ResponseDto(true, "Paciente agregado exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[PacienteController] Error en handleAdd: " + e.getMessage());
            return new ResponseDto(false, "Error agregando paciente: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleUpdate(RequestDto request) {
        try {
            UpdatePacienteRequestDto dto = gson.fromJson(request.getData(), UpdatePacienteRequestDto.class);
            LocalDate fechaNacimiento = LocalDate.parse(dto.getFechaNacimiento());

            Paciente updated = pacienteService.update(
                    Long.valueOf(dto.getId()),
                    dto.getNombre(),
                    dto.getTelefono(),
                    fechaNacimiento
            );

            if (updated == null) {
                return new ResponseDto(false, "Paciente no encontrado", null);
            }

            PacienteResponseDto response = toResponseDto(updated);
            return new ResponseDto(true, "Paciente actualizado exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[PacienteController] Error en handleUpdate: " + e.getMessage());
            return new ResponseDto(false, "Error actualizando paciente: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleDelete(RequestDto request) {
        try {
            DeletePacienteRequestDto dto = gson.fromJson(request.getData(), DeletePacienteRequestDto.class);
            boolean deleted = pacienteService.delete(Long.valueOf(dto.getId()));

            if (!deleted) {
                return new ResponseDto(false, "Paciente no encontrado", null);
            }

            return new ResponseDto(true, "Paciente eliminado exitosamente", null);
        } catch (Exception e) {
            System.err.println("[PacienteController] Error en handleDelete: " + e.getMessage());
            return new ResponseDto(false, "Error eliminando paciente: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleList(RequestDto request) {
        try {
            List<Paciente> pacientes = pacienteService.getAll();
            List<PacienteResponseDto> pacienteDtos = pacientes.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListPacienteResponseDto response = new ListPacienteResponseDto(pacienteDtos);
            return new ResponseDto(true, "Pacientes obtenidos exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[PacienteController] Error en handleList: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo pacientes: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleGet(RequestDto request) {
        try {
            DeletePacienteRequestDto dto = gson.fromJson(request.getData(), DeletePacienteRequestDto.class);
            Paciente paciente = pacienteService.getById(Long.valueOf(dto.getId()));

            if (paciente == null) {
                return new ResponseDto(false, "Paciente no encontrado", null);
            }

            PacienteResponseDto response = toResponseDto(paciente);
            return new ResponseDto(true, "Paciente obtenido exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[PacienteController] Error en handleGet: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo paciente: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleBuscarPorNombre(RequestDto request) {
        try {
            // Esperando un objeto JSON como {"nombre": "Juan"}
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            String nombre = (String) params.get("nombre");

            List<Paciente> pacientes = pacienteService.buscarPorNombre(nombre);
            List<PacienteResponseDto> pacienteDtos = pacientes.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListPacienteResponseDto response = new ListPacienteResponseDto(pacienteDtos);
            return new ResponseDto(true, "Pacientes encontrados", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[PacienteController] Error en handleBuscarPorNombre: " + e.getMessage());
            return new ResponseDto(false, "Error buscando pacientes: " + e.getMessage(), null);
        }
    }

    private PacienteResponseDto toResponseDto(Paciente paciente) {
        return new PacienteResponseDto(
                paciente.getId().intValue(),
                paciente.getNombre(),
                paciente.getTelefono(),
                paciente.getFechaNacimiento().toString()
        );
    }
}