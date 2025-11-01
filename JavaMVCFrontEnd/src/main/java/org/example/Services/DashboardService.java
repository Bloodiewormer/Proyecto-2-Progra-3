package org.example.Services;

import org.example.Domain.Dtos.Receta.RecetaResponseDto;
import org.example.Domain.Dtos.Medicamento.MedicamentoResponseDto;
import org.example.Domain.Dtos.RequestDto;
import org.example.Domain.Dtos.ResponseDto;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DashboardService extends BaseService {
    private final ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

    public DashboardService(String host, int port) {
        super(host, port);
    }

    public Future<DashboardDataDto> getDashboardDataAsync(String startDate, String endDate) {
        return executor.submit(() -> {
            RequestDto request = new RequestDto(
                    "Dashboard",
                    "getData",
                    gson.toJson(new DashboardRequestDto(startDate, endDate)),
                    null
            );
            ResponseDto response = sendRequest(request);
            if (!response.isSuccess()) return null;
            return gson.fromJson(response.getData(), DashboardDataDto.class);
        });
    }

    // DTO interno para la solicitud
    private static class DashboardRequestDto {
        private final String startDate;
        private final String endDate;

        public DashboardRequestDto(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
    }

    // DTO para la respuesta del dashboard
    public static class DashboardDataDto {
        private int totalRecetas;
        private int recetasPendientes;
        private int recetasCompletadas;
        private int medicamentosActivos;

        public DashboardDataDto() {}

        public int getTotalRecetas() { return totalRecetas; }
        public void setTotalRecetas(int totalRecetas) { this.totalRecetas = totalRecetas; }

        public int getRecetasPendientes() { return recetasPendientes; }
        public void setRecetasPendientes(int recetasPendientes) { this.recetasPendientes = recetasPendientes; }

        public int getRecetasCompletadas() { return recetasCompletadas; }
        public void setRecetasCompletadas(int recetasCompletadas) { this.recetasCompletadas = recetasCompletadas; }

        public int getMedicamentosActivos() { return medicamentosActivos; }
        public void setMedicamentosActivos(int medicamentosActivos) { this.medicamentosActivos = medicamentosActivos; }
    }
}