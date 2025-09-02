package org.example.data_access_layer;

import java.util.List;

public interface IFileStore<T> {
    /**
     * Lee todos los datos del tipo T guardados en el archivo.
     * @return Lista de elementos de tipo T.
     */
    List<T> readAll();
    /**
     * Escribe todos los datos del tipo indicado en el archivo.
     * @param data Los datos de tipo T.
     */
    void writeAll(List<T> data);
}
