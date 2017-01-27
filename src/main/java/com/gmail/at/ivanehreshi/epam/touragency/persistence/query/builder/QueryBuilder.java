package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.*;

/**
 * QueryBuilder helps to build up a dynamic query in
 * a step by step way
 */
public class QueryBuilder {
    protected SelectQuery query;

    QueryBuilder(SelectQuery query) {
        this.query = query;
    }

    public static WhereBuilder select(String t, String... cols) {
        return new WhereBuilder(new SelectQuery(t, cols));
    }

    public String build() {
        return query.getSQL();
    }
}
