package com.medkit.repository.interfaces;

import com.medkit.model.User;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Getter
public abstract class OracleRepositoryBase<T> implements OracleRepository<T>{

    protected Connection connection;

    public OracleRepositoryBase(Connection connection){
        this.connection = connection;
    }

    @Override
    public abstract void insert(T element) throws SQLException;

    @Override
    public abstract void delete(T element) throws SQLException;

    @Override
    public abstract void update(T element) throws SQLException;

    @Override
    public abstract T get(T element) throws SQLException;

    @Override
    public abstract List<T> getAll() throws SQLException;

    protected abstract List<T> convertResultSet(ResultSet resultSet) throws SQLException;
}
