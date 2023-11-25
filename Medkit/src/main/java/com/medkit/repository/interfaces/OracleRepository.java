package com.medkit.repository.interfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OracleRepository<T> {

    void insert(T element) throws SQLException;

    void delete(T element) throws SQLException;

    void update(T element) throws SQLException;

    T get(T element) throws SQLException;

    T getById(int id) throws SQLException;

    List<T> getAll() throws SQLException;
}
