package com.gmail.at.ivanehreshi.epam.touragency.persistence.query;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class QueryBuilderTest {
    @Test
    public void testSimpleSelect() {
        QueryBuilder builder = QueryBuilder.select("test");
        assertFuzzyEquals("SELECT * FROM test", builder.build());
    }

    @Test
    public void testSimpleWhere() {
        QueryBuilder builder = QueryBuilder.select("test").where("col", "> 1");
        assertFuzzyEquals("SELECT * FROM test WHERE col > 1", builder.build());
    }

    @Test
    public void testCompositeWhere() {
        QueryBuilder builder = QueryBuilder.select("test").where("col", "> 1")
                .and("col", "<> 3");
        assertFuzzyEquals("SELECT * FROM test WHERE col > 1 AND col <> 3",
                builder.build());
    }

    @Test
    public void testLimit() {
        QueryBuilder builder = QueryBuilder.select("test").limit(1);
        assertFuzzyEquals("SELECT * FROM test LIMIT 1", builder.build());
    }

    @Test
    public void testSimpleOrderBy() {
        QueryBuilder builder = QueryBuilder.select("test").orderBy("col", Ordering.DESC);
        assertFuzzyEquals("SELECT * FROM test ORDER BY col DESC", builder.build());
    }

    @Test
    public void testCompositeOrderBy() {
        QueryBuilder builder = QueryBuilder.select("test").orderBy("col1", Ordering.DESC)
                .and("col2", Ordering.ASC);
        assertFuzzyEquals("SELECT * FROM test ORDER BY col1 DESC, col2 ASC",
                builder.build());
    }

    @Test
    public void testWhereLimit() {
        QueryBuilder builder = QueryBuilder.select("test").where("col1", "<= 10")
                .and("col2", "<= 20").limit(1);
        assertFuzzyEquals("SELECT * FROM test WHERE col1 <= 10 AND col2 <= 20 LIMIT 1",
                builder.build());
    }

    @Test
    public void testFullQuery() {
        QueryBuilder builder = QueryBuilder.select("test").where("col1", "<= 10")
                .and("col2", "<= 20").orderBy("col1", Ordering.ASC)
                .and("col2", Ordering.DESC).limit(1);
        assertFuzzyEquals("SELECT * FROM test WHERE col1 <= 10 AND col2 <= 20 " +
                "ORDER BY col1 ASC, col2 DESC LIMIT 1", builder.build());
    }

    @Test
    public void testWhereInCondition() {
        QueryBuilder builder = QueryBuilder.select("test").where("col1", "<= 10")
                .and("col2", WhereBuilder.inCond(Arrays.asList("1", "2", "3")));
        assertFuzzyEquals("SELECT * FROM test WHERE col1 <= 10 AND col2 IN (1,2,3)",
                builder.build());
    }

    private static void assertFuzzyEquals(String expected, String actual) {
        assertEquals(expected.toLowerCase(), actual.replaceAll("\\s+", " ").trim().toLowerCase());
    }
}
