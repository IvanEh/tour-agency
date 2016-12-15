package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.SelectQuery;

public abstract class QueryBuilder {
    protected SelectQuery query;

    public QueryBuilder(SelectQuery query) {
        this.query = query;
    }

    public String build() {
        return query.getSQL();
    }

    public static SelectBuilder select(String table) {
        return new SelectBuilder(new SelectQuery(table));
    }
}