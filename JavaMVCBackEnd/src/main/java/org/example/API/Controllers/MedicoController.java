package org.example.API.Controllers;

import com.google.gson.Gson;
import org.example.DataAcces.services.UsuarioService;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;
import org.example.Domain.Dtos.Medico.*;
import org.example.Domain.models.Medico;

import java.util.List;
import java.util.stream.Collectors;

public class MedicoController {
    private final UsuarioService usuarioService;
    private final Gson gson = new Gson();

    public MedicoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
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
                case "buscar":
                    return handleBuscar(request);
                default:
                    return new ResponseDto(false, "Unknown request: " + request.getRequest(), null);
            }
        } catch (Exception e) {
            return new ResponseDto(false, e.getMessage(), null);
        }
    }

    private ResponseDto handleAdd(RequestDto request) {
        try {
            AddMedicoRequestDto dto = gson.fromJson(request.getData(), AddMedicoRequestDto.class);
            Medico medico = usuarioService.createMedico(
                    dto.getNombre(),
                    dto.getClave(),
                    dto.getEspecialidad()
            );

            MedicoResponseDto response = toResponseDto(medico);
            return new ResponseDto(true, "Médico agregado exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicoController] Error en handleAdd: " + e.getMessage());
            return new ResponseDto(false, "Error agregando médico: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleUpdate(RequestDto request) {
        try {
            UpdateMedicoRequestDto dto = gson.fromJson(request.getData(), UpdateMedicoRequestDto.class);
            Medico updated = usuarioService.updateMedico(
                    Long.valueOf(dto.getId()),
                    dto.getNombre(),
                    dto.getClave(),
                    dto.getEspecialidad()
            );

            if (updated == null) {
                return new ResponseDto(false, "Médico no encontrado", null);
            }

            MedicoResponseDto response = toResponseDto(updated);
            return new ResponseDto(true, "Médico actualizado exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicoController] Error en handleUpdate: " + e.getMessage());
            return new ResponseDto(false, "Error actualizando médico: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleDelete(RequestDto request) {
        try {
            DeleteMedicoRequestDto dto = gson.fromJson(request.getData(), DeleteMedicoRequestDto.class);
            boolean deleted = usuarioService.deleteMedico(Long.valueOf(dto.getId()));

            if (!deleted) {
                return new ResponseDto(false, "Médico no encontrado", null);
            }

            return new ResponseDto(true, "Médico eliminado exitosamente", null);
        } catch (Exception e) {
            System.err.println("[MedicoController] Error en handleDelete: " + e.getMessage());
            return new ResponseDto(false, "Error eliminando médico: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleList(RequestDto request) {
        try {
            List<Medico> medicos = usuarioService.getAllMedicos();
            List<MedicoResponseDto> medicoDtos = medicos.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListMedicoResponseDto response = new ListMedicoResponseDto(medicoDtos);
            return new ResponseDto(true, "Médicos obtenidos exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicoController] Error en handleList: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo médicos: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleGet(RequestDto request) {
        try {
            DeleteMedicoRequestDto dto = gson.fromJson(request.getData(), DeleteMedicoRequestDto.class);
            Medico medico = usuarioService.getMedicoById(Long.valueOf(dto.getId()));

            if (medico == null) {
                return new ResponseDto(false, "Médico no encontrado", null);
            }

            MedicoResponseDto response = toResponseDto(medico);
            return new ResponseDto(true, "Médico obtenido exitosamente", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicoController] Error en handleGet: " + e.getMessage());
            return new ResponseDto(false, "Error obteniendo médico: " + e.getMessage(), null);
        }
    }

    private ResponseDto handleBuscar(RequestDto request) {
        try {
            var params = gson.fromJson(request.getData(), java.util.Map.class);
            String filtro = (String) params.get("filtro");

            // Buscar por nombre (podrías extender para buscar por especialidad también)
            List<Medico> medicos = usuarioService.getAllMedicos().stream()
                    .filter(m -> m.getNombre().toLowerCase().contains(filtro.toLowerCase()) ||
                            m.getEspecialidad().toLowerCase().contains(filtro.toLowerCase()))
                    .collect(Collectors.toList());

            List<MedicoResponseDto> medicoDtos = medicos.stream()
                    .map(this::toResponseDto)
                    .collect(Collectors.toList());

            ListMedicoResponseDto response = new ListMedicoResponseDto(medicoDtos);
            return new ResponseDto(true, "Médicos encontrados", gson.toJson(response));
        } catch (Exception e) {
            System.err.println("[MedicoController] Error en handleBuscar: " + e.getMessage());
            return new ResponseDto(false, "Error buscando médicos: " + e.getMessage(), null);
        }
    }

    private MedicoResponseDto toResponseDto(Medico medico) {
        return new MedicoResponseDto(
                medico.getId().intValue(),
                medico.getNombre(),
                medico.getEspecialidad()
        );
    }
}