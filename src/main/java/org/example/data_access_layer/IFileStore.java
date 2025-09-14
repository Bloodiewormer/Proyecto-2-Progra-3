package org.example.data_access_layer;

import java.util.List;

public interface IFileStore<T> {

    List<T> readAll();
    void writeAll(List<T> data);

}