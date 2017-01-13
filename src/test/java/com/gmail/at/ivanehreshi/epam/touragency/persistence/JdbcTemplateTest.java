package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class JdbcTemplateTest {
    private static final String SQL_SELECT_ALL = "SELECT * FROM test";

    private ConnectionManager connectionManager;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        connectionManager = H2Db.init("test.sql");
        jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    @Test
    public void testQuery() {
        jdbcTemplate.query(rs -> {
            rs.next();
            assertEquals("x", rs.getString("col"));
            rs.next();
            assertEquals("y", rs.getString("col"));
            rs.next();
            assertEquals("z", rs.getString("col"));
        }, SQL_SELECT_ALL);
    }

    @Test
    public void testQueryObject() {
        String s = jdbcTemplate.queryObject(rs -> rs.getString("col"), SQL_SELECT_ALL);
        assertEquals("x", s);
    }

    @Test
    public void testQueryObjects() {
        List<String> cols = jdbcTemplate.queryObjects(rs -> rs.getString("col"),
                SQL_SELECT_ALL);

        assertEquals(Arrays.asList("x", "y", "z"), cols);
    }

    @Test
    public void testUpdate() {
        int count = jdbcTemplate.update("DELETE FROM test WHERE id >= 2 ");
        List<String> cols = jdbcTemplate.queryObjects(rs -> rs.getString("col"),
                SQL_SELECT_ALL);

        assertEquals(Arrays.asList("x"), cols);
        assertEquals(2, count);
    }

    @Test
    public void testInsert() {
        Long id = jdbcTemplate.insert("INSERT INTO test(col) VALUES(?)", "alpha");
        List<String> cols = jdbcTemplate.queryObjects(rs -> rs.getString("col"), SQL_SELECT_ALL);

        assertEquals(Long.valueOf(4), id);
        assertEquals(Arrays.asList("x", "y", "z", "alpha"), cols);
    }

    @Test
    public void testTxImplicitRollback() {
        JdbcTemplate txTemplate = new JdbcTemplate(connectionManager);
        txTemplate.startTransaction();

        Long id1 = txTemplate.insert("INSERT INTO test(col) VALUES(?)", "alpha");
        Long id2 = txTemplate.insert("INSERT INTO test(col, id) VALUES(?, ?)", null, 1);

        txTemplate.commit();

        assertEquals(Long.valueOf(4), id1);
        assertNull(id2);

        List<String> cols = jdbcTemplate.queryObjects(rs -> rs.getString("col"),
                SQL_SELECT_ALL);
        assertEquals(Arrays.asList("x", "y", "z"), cols);
    }

    @Test
    public void testTxExplicitRollback() {
        JdbcTemplate txTemplate = new JdbcTemplate(connectionManager);
        txTemplate.startTransaction();

        Long id1 = txTemplate.insert("INSERT INTO test(col) VALUES(?)", "alpha");

        txTemplate.rollback();

        Long id2 = txTemplate.insert("INSERT INTO test(col) VALUES(?)", "beta");

        txTemplate.commit();

        assertEquals(Long.valueOf(4), id1);

        List<String> cols = jdbcTemplate.queryObjects(rs -> rs.getString("col"),
                SQL_SELECT_ALL);
        assertEquals(Arrays.asList("x", "y", "z"), cols);
    }
}