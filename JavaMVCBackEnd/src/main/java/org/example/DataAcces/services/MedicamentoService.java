package org.example.DataAcces.services;

import org.example.Domain.models.Medicamento;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class MedicamentoService {
    private final SessionFactory sessionFactory;

    public MedicamentoService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Medicamento create(String nombre, String presentacion) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Medicamento medicamento = new Medicamento(nombre, presentacion);
            session.persist(medicamento);
            tx.commit();
            return medicamento;
        }
    }

    public Medicamento getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Medicamento.class, id);
        }
    }

    public List<Medicamento> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Medicamento", Medicamento.class).list();
        }
    }

    public Medicamento update(Long id, String nombre, String presentacion) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Medicamento medicamento = session.get(Medicamento.class, id);
            if (medicamento != null) {
                medicamento.setNombre(nombre);
                medicamento.setPresentacion(presentacion);
                session.merge(medicamento);
            }
            tx.commit();
            return medicamento;
        }
    }

    public boolean delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Medicamento medicamento = session.get(Medicamento.class, id);
            if (medicamento != null) {
                session.remove(medicamento);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        }
    }
}