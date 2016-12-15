package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JdbcTemplate {
    private static final Logger LOGGER = LogManager.getLogger(JdbcTemplate.class);

    private ConnectionManager connectionManager;

    public JdbcTemplate(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    public Connection getConnection() {
        return this.connectionManager.getConnection();
    }

    public <R> void query(ResultSetFunction<R> fn, String query, Object... params) {
        Connection conn = getConnection();

        if(conn == null)
            return;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            ResultSet rs = stmt.executeQuery();
            withRs(rs, fn);
        } catch (SQLException e) {
            LOGGER.warn("Error creating prepared statement. Query: " + query, e);
        } finally {
            close(conn);
        }
    }

    public <R> List<R> queryObjects(ResultSetFunction<R> producer, String query, Object... params) {
        List<R> entities = new ArrayList<>();

        query((rs) -> {
            while (rs.next()) {
                entities.add(producer.apply(rs));
            }
            return null;
        }, query, params);

        return entities;
    }

    public <R> R queryObject(ResultSetFunction<R> producer, String query, Object... params) {
        Object[] r = new Object[]{null};
        query((rs) -> {
            if (rs.next()) {
                r[0] = producer.apply(rs);
            }
            return null;
        }, query, params);

        return (R) r[0];
    }

    public int update(String updQuery, Object... params) {
        Connection conn = getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(updQuery)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Cannot execute update query", e);
        }

        return 0;
    }

    public Long insert(String updQuery, Object... params) {
        Connection conn = getConnection();

        try (PreparedStatement stmt
                     = conn.prepareStatement(updQuery, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()) {
                return rs.getLong(1);
            }

            return null;

        } catch (SQLException e) {
            LOGGER.error("Cannot insert values into DB", e);
        }

        return null;
    }

    public <R> R withRs(ResultSet rs, ResultSetFunction<R> fn) {
        try {
            return fn.apply(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error("Cannot close ResultSet", e);
            }
        }

        return null;
    }


    public boolean executeSqlFile(File file) {
        try {
            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()));
                List<String> queries = Arrays.asList(content.split(";"))
                        .stream()
                        .map(s -> s.trim())
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());

                Connection conn = getConnection();

                for(String query: queries) {
                    try (Statement stmt = conn.createStatement()) {
                        String tr = query.trim();
                        stmt.execute(query.trim());
                    }
                }

                return true;
            }


        } catch (IOException e) {
            LOGGER.warn("SQL script file not found");
            return false;
        }catch (SQLException e) {
            LOGGER.error("Errors while executing SQL script " + file.getAbsolutePath(), e);
            return false;
        }

        return false;
    }

    public void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Cannot close jdbc connection", e);
        }
    }

    @FunctionalInterface
    public interface ResultSetFunction<R>{
        R apply(ResultSet resultSet) throws SQLException;
    }
}
