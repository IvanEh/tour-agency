package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.condition;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.*;

import java.util.*;

public class OrCondition implements BoolCondition {
    private List<Condition> conditions;

    public OrCondition(Condition... conditions) {
        if(conditions.length < 2) {
            throw new IllegalArgumentException();
        }

        this.conditions = Arrays.asList(conditions);
    }

    @Override
    public String getSQL() {
        String s = conditions.stream()
                .map(c -> "(" + c.getSQL() + ")")
                .reduce((acc, el) -> acc + " OR " + el)
                .get();

        return s;
    }
}
