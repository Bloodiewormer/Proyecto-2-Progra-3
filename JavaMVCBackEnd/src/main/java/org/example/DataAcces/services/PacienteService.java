package org.example.DataAcces.services;

import org.example.Domain.Dtos.models.Paciente;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class PacienteService {
    private final SessionFactory sessionFactory;

    public PacienteService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Paciente create(String nombre, String telefono, LocalDate fechaNacimiento) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Paciente paciente = new Paciente(nombre, telefono, fechaNacimiento);
            session.persist(paciente);
            tx.commit();
            return paciente;
        }
    }

    public Paciente getById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Paciente.class, id);
        }
    }

    public List<Paciente> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Paciente", Paciente.class).list();
        }
    }

    public List<Paciente> buscarPorNombre(String nombre) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "FROM Paciente WHERE LOWER(nombre) LIKE LOWER(:nombre)",
                            Paciente.class
                    )
                    .setParameter("nombre", "%" + nombre + "%")
                    .list();
        }
    }

    public Paciente update(Long id, String nombre, String telefono, LocalDate fechaNacimiento) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Paciente paciente = session.get(Paciente.class, id);
            if (paciente != null) {
                paciente.setNombre(nombre);
                paciente.setTelefono(telefono);
                paciente.setFechaNacimiento(fechaNacimiento);
                session.merge(paciente);
            }
            tx.commit();
            return paciente;
        }
    }

    public boolean delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Paciente paciente = session.get(Paciente.class, id);
            if (paciente != null) {
                session.remove(paciente);
                tx.commit();
                return true;
            }
            tx.rollback();
            return false;
        }
    }
}

// Revisar