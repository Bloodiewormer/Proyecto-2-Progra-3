package org.example.DataAcces.services;

import org.example.Domain.models.*;
import org.example.Utilities.EstadoReceta;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class RecetaService {
    private final SessionFactory sessionFactory;

    public RecetaService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Receta create(Long pacienteId, Long medicoId, LocalDateTime fechaRetiro) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Paciente paciente = session.get(Paciente.class, pacienteId);
            Medico medico = session.get(Medico.class, medicoId);

            if (paciente == null || medico == null) {
                tx.rollback();
                return null;
            }

            Receta receta = new Receta();
            receta.setPaciente(paciente);
            receta.setMedico(medico);
            receta.setFechaRetiro(fechaRetiro);
            receta.setEstado(EstadoReceta.CONFECCIONADA);

            session.persist(receta);
            tx.commit();
            return receta;
        }
    }

    public Receta getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Receta receta = session.get(Receta.class, id);
            if (receta != null) {
                Hibernate.initialize(receta.getDetalles());
                Hibernate.initialize(receta.getPaciente());
                Hibernate.initialize(receta.getMedico());
            }
            return receta;
        }
    }

    public List<Receta> getAll() {
        try (Session session = sessionFactory.openSession()) {
            List<Receta> recetas = session.createQuery("FROM Receta", Receta.class).list();
            recetas.forEach(r -> {
                Hibernate.initialize(r.getDetalles());
                Hibernate.initialize(r.getPaciente());
                Hibernate.initialize(r.getMedico());
            });
            return recetas;
        }
    }

    public List<Receta> getByPaciente(Long pacienteId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Receta WHERE paciente.id = :pacienteId",
                            Receta.class
                    )
                    .setParameter("pacienteId", pacienteId)
                    .list();
        }
    }

    public List<Receta> getByEstado(EstadoReceta estado) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Receta WHERE estado = :estado",
                            Receta.class
                    )
                    .setParameter("estado", estado)
                    .list();
        }
    }

    public Receta updateEstado(Long id, EstadoReceta nuevoEstado) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Receta receta = session.get(Receta.class, id);
            if (receta != null) {
                receta.setEstado(nuevoEstado);
                session.merge(receta);
            }
            tx.commit();
            return receta;
        }
    }

    public void addDetalle(Long recetaId, Long medicamentoId, int cantidad, String indicaciones, int dias) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Receta receta = session.get(Receta.class, recetaId);
            Medicamento medicamento = session.get(Medicamento.class, medicamentoId);

            if (receta != null && medicamento != null) {
                DetalleReceta detalle = new DetalleReceta(medicamento, cantidad, indicaciones, dias);
                receta.addDetalle(detalle);
                session.merge(receta);
            }

            tx.commit();
        }
    }

    public boolean delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Receta receta = session.get(Receta.class, id);
            if (receta != null) {
                session.remove(receta);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        }
    }
}