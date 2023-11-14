package com.medkit.repository.interfaces;

import java.sql.Connection;
import java.util.List;

public interface OracleRepository<T> {

    void insert(T element);

    void delete(T element);

    void update(T element);

    T get(T element);

    List<T> getAll();
}
