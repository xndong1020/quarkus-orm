package org.jeremygu.quarkus.starting.repos;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;

@ApplicationScoped
public class DatabaseUtil {

    @Inject
    AgroalDataSource dataSource;

    public void executeSql(String sql) throws SQLException {
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
}
