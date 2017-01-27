package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.condition.*;

public class WhereBuilder extends OrderByBuilder {
    WhereBuilder(SelectQuery query) {
        super(query);
    }

    public OrderByBuilder where(BoolCondition condition) {
        return new OrderByBuilder(query.where(condition));
    }

    public WhereBuilder and(BoolCondition condition) {
        if(query.getWhereClause() == null) {
            return new WhereBuilder(query.where(condition));
        } else {
            BoolCondition andCond = new AndCondition(query.getWhereClause(), condition);
            return new WhereBuilder(query.where(andCond));
        }
    }

    public static RelationCondition rel(String col, Ordering ord, int value) {
        return new RelationCondition(col, ord, value);
    }

    public static InCondition in(String col, boolean quote, String... vals) {
        return new InCondition(col, quote, vals);
    }

    public static OrCondition or(Condition... conditions) {
        return new OrCondition(conditions);
    }
}
