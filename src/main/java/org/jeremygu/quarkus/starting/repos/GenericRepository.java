package org.jeremygu.quarkus.starting.repos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    List<T> findAll() throws SQLException;

    Optional<T> findById(ID id);

    T save(T entity);

    void delete(T entity);

    long count() throws SQLException;
}
