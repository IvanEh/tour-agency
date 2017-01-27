package com.gmail.at.ivanehreshi.epam.touragency.persistence.query.condition;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public class OrderByCondition implements Condition {
    private String col;

    private SortDir sortDir;

    public OrderByCondition(String col, SortDir sortDir) {
        this.col = col;
        this.sortDir = sortDir;
    }

    @Override
    public String getSQL() {
        String dir = sortDir.getName();

        if(!dir.isEmpty())
            dir = " " + dir;

        return col + dir;
    }
}
