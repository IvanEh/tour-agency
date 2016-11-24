package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    public static final Logger LOGGER = LogManager.getLogger(ConnectionManager.class);

    private static final java.lang.String JDBC_URL = "jdbc.url";
    private static final java.lang.String JDBC_USER = "jdbc.user";
    private static final java.lang.String JDBC_PASSWORD = "jdbc.password";
    private static final java.lang.String JDBC_POOL = "jdbc.pool";

    private static final String DEFAULT_POOL_SIZE = "1";

    private DataSource dataSource;
    private int poolSize;


    public ConnectionManager() {
        MysqlDataSource mysqlDs = new MysqlDataSource();

        Properties props = loadProperties();

        mysqlDs.setUrl(props.getProperty(JDBC_URL));
        mysqlDs.setUser(props.getProperty(JDBC_USER, "root"));
        mysqlDs.setPassword(props.getProperty(JDBC_PASSWORD, "root"));
        poolSize = Integer.parseInt(props.getProperty(JDBC_POOL, DEFAULT_POOL_SIZE));

        dataSource = mysqlDs;
    }


    private Properties loadProperties() {
        Properties properties = new Properties();

        try(InputStream inputStream = new FileInputStream("database.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.warn("Cannot load database.properties", e);
        }

        return properties;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Cannot create connection to the database", e);
        }

        return null;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
