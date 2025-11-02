package org.example.Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Domain.Dtos.Medicamento.MedicamentoResponseDto;
import org.example.Domain.Dtos.Paciente.PacienteResponseDto;
import org.example.Domain.Dtos.Receta.AddRecetaRequestDto;
import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.lang.reflect.Type;
import java.util.List;

public class PrescribirService extends BaseService {
    private final Gson gson = new Gson();

    public PrescribirService(String host, int port) {
        super(host, port);
    }

    public List<PacienteResponseDto> listarPacientes() {
        RequestDto request = new RequestDto("Prescribir", "listarPacientes", "", null);
        ResponseDto response = sendRequest(request);
        if (!response.isSuccess()) return null;

        Type listType = new TypeToken<List<PacienteResponseDto>>(){}.getType();
        return gson.fromJson(response.getData(), listType);
    }

    public List<MedicamentoResponseDto> listarMedicamentos() {
        RequestDto request = new RequestDto("Prescribir", "listarMedicamentos", "", null);
        ResponseDto response = sendRequest(request);
        if (!response.isSuccess()) return null;

        Type listType = new TypeToken<List<MedicamentoResponseDto>>(){}.getType();
        return gson.fromJson(response.getData(), listType);
    }

    public RecetaResponseDto crearReceta(AddRecetaRequestDto dto) {
        RequestDto request = new RequestDto("Prescribir", "crear", gson.toJson(dto), null);
        ResponseDto response = sendRequest(request);
        if (!response.isSuccess()) return null;

        return gson.fromJson(response.getData(), RecetaResponseDto.class);
    }
}