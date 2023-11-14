package com.medkit.repository.interfaces;

import lombok.Getter;

import java.sql.Connection;
import java.util.List;

@Getter
public abstract class OracleRepositoryBase<T> implements OracleRepository<T>{

    protected Connection connection;

    public OracleRepositoryBase(Connection connection){
        this.connection = connection;
    }

    @Override
    public abstract void insert(T element);

    @Override
    public abstract void delete(T element);

    @Override
    public abstract void update(T element);

    @Override
    public abstract T get(T element);

    @Override
    public abstract List<T> getAll();
}
