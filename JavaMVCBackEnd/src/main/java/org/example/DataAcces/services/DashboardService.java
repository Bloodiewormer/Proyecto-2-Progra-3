package org.example.DataAcces.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DashboardService {
    private final SessionFactory sessionFactory;

    public DashboardService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // -------------------------
    // READ
    // -------------------------
    public DashboardData getDashboardData(String startDate, String endDate) {
        try (Session session = sessionFactory.openSession()) {
            // Total de recetas en el rango
            Long totalRecetas = (Long) session.createQuery(
                            "SELECT COUNT(r) FROM Receta r WHERE r.fecha BETWEEN :start AND :end")
                    .setParameter("start", startDate)
                    .setParameter("end", endDate)
                    .uniqueResult();

            // Recetas pendientes
            Long recetasPendientes = (Long) session.createQuery(
                            "SELECT COUNT(r) FROM Receta r WHERE r.estado = 'PENDIENTE' AND r.fecha BETWEEN :start AND :end")
                    .setParameter("start", startDate)
                    .setParameter("end", endDate)
                    .uniqueResult();

            // Recetas completadas
            Long recetasCompletadas = (Long) session.createQuery(
                            "SELECT COUNT(r) FROM Receta r WHERE r.estado = 'COMPLETADA' AND r.fecha BETWEEN :start AND :end")
                    .setParameter("start", startDate)
                    .setParameter("end", endDate)
                    .uniqueResult();

            // Medicamentos activos
            Long medicamentosActivos = (Long) session.createQuery(
                            "SELECT COUNT(m) FROM Medicamento m WHERE m.activo = true")
                    .uniqueResult();

            return new DashboardData(
                    totalRecetas != null ? totalRecetas.intValue() : 0,
                    recetasPendientes != null ? recetasPendientes.intValue() : 0,
                    recetasCompletadas != null ? recetasCompletadas.intValue() : 0,
                    medicamentosActivos != null ? medicamentosActivos.intValue() : 0
            );
        } catch (Exception e) {
            String message = String.format("An error occurred when processing: %s. Details: %s", "getDashboardData", e);
            System.out.println(message);
            throw e;
        }
    }

    // -------------------------
    // DTO interno para la respuesta del dashboard
    // -------------------------
    public static class DashboardData {
        private int totalRecetas;
        private int recetasPendientes;
        private int recetasCompletadas;
        private int medicamentosActivos;

        public DashboardData(int totalRecetas, int recetasPendientes, int recetasCompletadas, int medicamentosActivos) {
            this.totalRecetas = totalRecetas;
            this.recetasPendientes = recetasPendientes;
            this.recetasCompletadas = recetasCompletadas;
            this.medicamentosActivos = medicamentosActivos;
        }

        public int getTotalRecetas() { return totalRecetas; }
        public int getRecetasPendientes() { return recetasPendientes; }
        public int getRecetasCompletadas() { return recetasCompletadas; }
        public int getMedicamentosActivos() { return medicamentosActivos; }
    }
}

