package com.gmail.at.ivanehreshi.epam.touragency.util;

public enum  Ordering {
    ASC("ASC"), DESC("DESC"), NO("");

    private final String name;

    Ordering(String asc) {
        this.name = asc;
    }

    public String getName() {
        return name;
    }
}
