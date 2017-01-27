package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.condition.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public class OrderByBuilder extends LimitBuilder {
    OrderByBuilder(SelectQuery query) {
        super(query);
    }

    public OrderByBuilder orderBy(OrderByCondition... conditions) {
        return new OrderByBuilder(query.orderBy(Arrays.asList(conditions)));
    }

    public OrderByBuilder orderBy(String col, SortDir sortDir) {
        return orderBy(new OrderByCondition(col, sortDir));
    }
}
