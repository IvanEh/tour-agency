package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.SelectQuery;
import com.gmail.at.ivanehreshi.epam.touragency.util.Ordering;

import java.util.List;

public class WhereBuilder extends QueryBuilder {

    public WhereBuilder(SelectQuery query) {
        super(query);
    }

    public WhereBuilder and(String col, String cond) {
        if(col == null || cond == null) {
            return this;
        }

        if(query.getWhereClause() != null && query.getWhereClause().length > 0) {
            return new WhereBuilder(query.addWhere("AND " + col + " " + cond));
        }

        return new WhereBuilder(query.addWhere(col + " " + cond));
    }

    public OrderByBuilder orderBy(String col, Ordering order) {
        OrderByBuilder orderByBuilder = new OrderByBuilder(query);
        return orderByBuilder.and(col, order);
    }

    public LimitBuilder limit(int count) {
        return new LimitBuilder(query, count);
    }

    public static String inCond(List<String> values) {
        if(values.isEmpty()) {
            return null;
        }
        return "IN (" + String.join(",", values) + ")";
    }
}
