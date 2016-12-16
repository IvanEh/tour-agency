package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.SelectQuery;
import com.gmail.at.ivanehreshi.epam.touragency.util.Ordering;

/**
 * QueryBuilder that represent OrderBy part of the query
 */
public class OrderByBuilder extends QueryBuilder {

    public OrderByBuilder(SelectQuery query) {
        super(query);
    }

    public OrderByBuilder and(String col, Ordering ord) {
        if (col == null || ord == null || ord == Ordering.NO) {
            return this;
        }

        if(query.getOrderByClause() != null && query.getOrderByClause().length > 0) {
            return new OrderByBuilder(query.addOrderBy(", " + col + " " + ord.name()));
        }

        return new OrderByBuilder(query.addOrderBy(col + " " + ord.name()));
    }

    public LimitBuilder limit(Integer count) {
        return new LimitBuilder(query, count);
    }
}
