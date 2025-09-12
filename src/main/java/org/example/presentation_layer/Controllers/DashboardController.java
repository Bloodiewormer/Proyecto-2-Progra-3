package org.example.presentation_layer.Controllers;

import org.example.domain_layer.Receta;
import org.example.domain_layer.Medicamento;
import org.example.service_layer.MedicamentoService;
import org.example.service_layer.RecetaService;
import org.example.utilities.EstadoReceta;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class DashboardController {

//    private final RecetaService recetaService;
//    private final MedicamentoService medicamentoService;
//
//    public DashboardController(RecetaService recetaService, MedicamentoService medicamentoService) {
//        this.recetaService = recetaService;
//        this.medicamentoService = medicamentoService;
//    }
//
//    /**
//     * Retorna un mapa con la cantidad de medicamentos prescritos por mes,
//     * filtrados por medicamento y rango de meses.
//     *
//     * @param medicamentoId ID del medicamento
//     * @param inicio rango de fecha inicial
//     * @param fin rango de fecha final
//     */
//    public Map<String, Long> getMedicamentosPorMes(int medicamentoId, LocalDate inicio, LocalDate fin) {
//        List<Receta> recetas = recetaService.leerTodos();
//
//        return recetas.stream()
//                .filter(r -> {
//                    LocalDate fecha = r.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                    return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
//                })
//                .filter(r -> r.getMedicamentos().stream().anyMatch(m -> m.getCodigo() == medicamentoId))
//                .collect(Collectors.groupingBy(
//                        r -> r.getFecha().getMonthValue() + "-" + r.getFecha().getYear(),
//                        Collectors.counting()
//                ));
//    }
//
//    /**
//     * Retorna un mapa con la cantidad de recetas agrupadas por estado.
//     *
//     * @return Map con EstadoReceta como clave y cantidad como valor
//     */
//    public Map<EstadoReceta, Long> getRecetasPorEstado() {
//        List<Receta> recetas = recetaService.leerTodos();
//
//        return recetas.stream()
//                .collect(Collectors.groupingBy(
//                        Receta::getEstado,
//                        Collectors.counting()
//                ));
//    }
//
//    /**
//     * Retorna todos los medicamentos disponibles para poblar un combo en la vista.
//     */
//    public List<Medicamento> getTodosLosMedicamentos() {
//        return medicamentoService.leerTodos();
//    }
}

