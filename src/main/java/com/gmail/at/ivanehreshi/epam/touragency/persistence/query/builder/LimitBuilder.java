package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.*;

/**
 * QueryBuilder that represent limit part of the query
 *
 * No further manipulation can be invoked so contains only
 * a #build method which retrieves the query
 */
public class LimitBuilder extends QueryBuilder {

    LimitBuilder(SelectQuery query) {
        super(query);
    }

    public QueryBuilder limit(Integer limit) {
        return new QueryBuilder(query.setLimit(limit));
    }
}
