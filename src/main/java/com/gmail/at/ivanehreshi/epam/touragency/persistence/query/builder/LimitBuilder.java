package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.SelectQuery;

public class LimitBuilder extends QueryBuilder {

    public LimitBuilder(SelectQuery query, Integer count) {
        super(query.setLimit(count));
    }
}
