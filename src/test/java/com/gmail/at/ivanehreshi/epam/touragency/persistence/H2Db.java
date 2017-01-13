package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.h2.jdbcx.*;

public class H2Db {
    public static ConnectionManager init(String initScript) {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:mem:test;Mode=MYSQL;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("sa");

        ConnectionManager connectionManager = new ConnectionManager(ds);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionManager);
        jdbcTemplate.executeSqlFile(ResourcesUtil.getResourceFile(initScript));

        return connectionManager;
    }
}
