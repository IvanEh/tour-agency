package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.SelectQuery;
import com.gmail.at.ivanehreshi.epam.touragency.util.Ordering;

/**
 * QueryBuilder that represent select-from part of the query
 */
public class SelectBuilder extends QueryBuilder {

    public SelectBuilder(SelectQuery query) {
        super(query);
    }

    public WhereBuilder where(String col, String cond) {
        WhereBuilder whereBuilder = new WhereBuilder(query);
        return whereBuilder.and(col, cond);
    }

    public OrderByBuilder orderBy(String col, Ordering ord) {
        OrderByBuilder orderByBuilder = new OrderByBuilder(query);
        return orderByBuilder.and(col, ord);
    }

    public LimitBuilder limit(int count) {
        LimitBuilder limitBuilder = new LimitBuilder(query, count);
        return limitBuilder;
    }
}
