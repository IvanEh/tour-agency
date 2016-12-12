package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import java.util.Arrays;

public enum HttpMethod {
    GET(1), POST(2), DELETE(4), PUT(8);

    public final int mask;

    public static final int ANY_METHOD_MASK;

    static {
        ANY_METHOD_MASK = Arrays.asList(HttpMethod.values()).stream()
                .map(m -> m.mask)
                .reduce((r1, r2) -> r1 | r2)
                .get();
    }

    HttpMethod(int mask) {
        this.mask = mask;
    }
}
