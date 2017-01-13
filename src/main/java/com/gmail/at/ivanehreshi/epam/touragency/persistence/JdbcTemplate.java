package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import org.apache.logging.log4j.*;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.stream.*;

/**
 * This class encapsulate a lot of boilerplate codes for JDBC
 * It also contains error handling and logging code
 */
public class JdbcTemplate {
    private static final Logger LOGGER = LogManager.getLogger(JdbcTemplate.class);

    private ConnectionManager connectionManager;

    /**
     * The ongoing transaction's connection. Normally this field is null
     * and a connection from connectionManager directly used and disposed
     */
    private Connection txConnection;

    public JdbcTemplate(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    public Connection getConnection() {
        if(txConnection != null) {
            return txConnection;
        }

        return this.connectionManager.getConnection();
    }

    public void startTransaction(int txIsolationLevel) {
        txConnection = getConnection();

        try {
            txConnection.setAutoCommit(false);
            txConnection.setTransactionIsolation(txIsolationLevel);
        } catch (SQLException e) {
            LOGGER.error("Cannot start transaction. Your application may be data inconsistent", e);
        }
    }

    public void startTransaction() {
        startTransaction(Connection.TRANSACTION_READ_COMMITTED);
    }

    public void commit() {
        if(txConnection == null) {
            LOGGER.error("Tried to commit transaction before starting one");
            return;
        }

        try {
            txConnection.commit();
            txConnection.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.error("Cannot commit");
            try {
                txConnection.rollback();
            } catch (SQLException e1) {
                LOGGER.error("", e1);
            }
        } finally {
            tryClose(txConnection);
        }
    }

    public void rollback() {
        if(txConnection != null) {
            try {
                txConnection.rollback();
            } catch (SQLException e) {
                LOGGER.error("Cannot call rollback", e);
            }
        } else {
            LOGGER.error("err");
        }
    }
    public void query(ResultSetFunction fn, String query, Object... params) {
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
            tryClose(conn);
        }
    }

    public <R> List<R> queryObjects(EntityExtractor<R> producer, String query, Object... params) {
        List<R> entities = new ArrayList<>();

        query(rs -> {
            while (rs.next()) {
                entities.add(producer.apply(rs));
            }
        }, query, params);

        return entities;
    }

    public <R> R queryObject(EntityExtractor<R> producer, String query, Object... params) {
        Object[] r = new Object[]{null};
        query((rs) -> {
            if (rs.next()) {
                r[0] = producer.apply(rs);
            }
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
        } finally {
            tryClose(conn);
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
        } finally {
            tryClose(conn);
        }

        return null;
    }

    public void withRs(ResultSet rs, ResultSetFunction fn) {
        try {
            fn.apply(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.error("Cannot tryClose ResultSet", e);
            }
        }
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

                try {
                    for (String query : queries) {
                        try (Statement stmt = conn.createStatement()) {
                            String tr = query.trim();
                            stmt.execute(query.trim());
                        }
                    }
                } finally {
                    tryClose(conn);
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


    // TODO: make private
    /**
     *
     * @param connection
     */
    public void tryClose(Connection connection) {
        try {
            if(txConnection != connection) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.error("Cannot tryClose jdbc connection", e);
        }
    }

    @FunctionalInterface
    public interface ResultSetFunction{
        void apply(ResultSet resultSet) throws SQLException;
    }

    @FunctionalInterface
    public interface EntityExtractor<R> {
        R apply(ResultSet resultSet) throws SQLException;
    }
}
