package org.example.service_layer;

import java.util.List;

public interface IService<T> {
    void agregar(T entity);
    void borrar(int id);
    void actualizar(T entity);
    List<T> leerTodos();

    T leerPorId(int id);

    void addObserver(IServiceObserver<T> listener);


}
