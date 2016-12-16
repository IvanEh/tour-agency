package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import com.gmail.at.ivanehreshi.epam.touragency.util.ResourcesUtil;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ConnectionManager encapsulates a data source. The static factory methods
 * allow creating a DataSource object from various sources
 */
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

    /**
     * Creates a ConnectionManager using MySQL JDBC connector driver
     * and loads its settings from a property file
     * @param filename
     * @return
     */
    public static ConnectionManager fromProperties(String filename) {
        ConnectionManager connManager = new ConnectionManager();
        MysqlDataSource mysqlDs = new MysqlDataSource();

        Properties props = loadProperties();

        mysqlDs.setUrl(props.getProperty(JDBC_URL));
        mysqlDs.setUser(props.getProperty(JDBC_USER, "root"));
        mysqlDs.setPassword(props.getProperty(JDBC_PASSWORD, "root"));

        connManager.poolSize = Integer.parseInt(props.getProperty(JDBC_POOL, DEFAULT_POOL_SIZE));
        connManager.dataSource = mysqlDs;
        return connManager;
    }

    /**
     * Creates a ConnectionManager from a JNDI path of the DataSource location
     * in the component's environment
     * @param name - JNDI name of the DataSource component
     * @return a new ConnectionManager
     */
    public static ConnectionManager fromJndi(String name) {
        try {
            Context initContext = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)envContext.lookup(name);
            ConnectionManager connManager = new ConnectionManager();
            connManager.dataSource = ds;
            return connManager;
        } catch (NamingException e) {
            LOGGER.error("Cannot create InitialContext", e);
            return null;
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        File file = ResourcesUtil.getResourceFile("database.properties");

        try(InputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.warn("Cannot load database.properties", e);
        }

        return properties;
    }
}
