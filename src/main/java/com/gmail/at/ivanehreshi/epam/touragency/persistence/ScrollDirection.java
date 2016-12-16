package com.gmail.at.ivanehreshi.epam.touragency.persistence;

/**
 * Represent scrolling direction of the database Slice
 * @see Slice
 */
public enum ScrollDirection {
    UP(">"), DOWN("<");

    private final String rel;

    ScrollDirection(String rel) {
        this.rel = rel;
    }

    public String getRel() {
        return rel;
    }

    public static ScrollDirection valueOf(int val) {
        if(val >= 0) {
            return DOWN;
        }

        return UP;
    }
}
