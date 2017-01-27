package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.condition;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.*;

public class LikeCondition implements BoolCondition {
    private final String col;

    private String term;

    public LikeCondition(String col, String term) {
        this.col = col;
        this.term = term;
    }

    @Override
    public String getSQL() {
        return col + " LIKE " + "'" + term + "'";
    }
}
