package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.condition;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.*;

public class AndCondition implements BoolCondition {
    private Condition left;
    private Condition right;

    public AndCondition(Condition left, Condition right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getSQL() {
        return String.format("(%s) AND (%s)", left.getSQL(), right.getSQL());
    }
}
