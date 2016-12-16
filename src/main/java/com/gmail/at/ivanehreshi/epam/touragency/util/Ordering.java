package com.gmail.at.ivanehreshi.epam.touragency.util;

/**
 * Represent ordering relationship between elements
 * Used for specifying order by clause in various DAO methods
 */
public enum  Ordering {
    ASC("ASC", "<"), DESC("DESC", ">"), NO("", "=");

    private final String name;
    private final String rel;

    Ordering(String name, String rel) {
        this.name = name;
        this.rel = rel;
    }

    public String getName() {
        return name;
    }

    public String getRel() {
        return rel;
    }
}
