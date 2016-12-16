package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.SelectQuery;

/**
 * QueryBuilder that represent limit part of the query
 *
 * No further manipulation can be invoked so contains only
 * a #build method which retrieves the query
 */
public class LimitBuilder extends QueryBuilder {

    public LimitBuilder(SelectQuery query, Integer count) {
        super(query.setLimit(count));
    }
}
