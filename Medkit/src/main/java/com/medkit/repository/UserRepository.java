package com.medkit.repository;

import com.medkit.model.User;
import com.medkit.repository.interfaces.OracleRepositoryBase;

import java.sql.Connection;
import java.util.List;

public class UserRepository extends OracleRepositoryBase<User> {
    public UserRepository(Connection connection) {
        super(connection);
    }

    @Override
    public void insert(User element) {

    }

    @Override
    public void delete(User element) {

    }

    @Override
    public void update(User element) {

    }

    @Override
    public User get(User element) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
