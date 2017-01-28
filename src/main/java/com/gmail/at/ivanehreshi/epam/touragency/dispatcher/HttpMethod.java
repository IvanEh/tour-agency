package com.gmail.at.ivanehreshi.epam.touragency.dispatcher;

import java.util.*;
import java.util.stream.*;

public enum HttpMethod {
    GET(1), POST(2), DELETE(4), PUT(8);

    private final int mask;

    private static final int ANY_METHOD_MASK;

    static {
        ANY_METHOD_MASK = Arrays.asList(HttpMethod.values()).stream()
                .map(m -> m.mask)
                .reduce((r1, r2) -> r1 | r2)
                .get();
    }

    HttpMethod(int mask) {
        this.mask = mask;
    }

    public static HttpMethodMask combine(HttpMethod... methods) {
        return new HttpMethodMask(
                Stream.of(methods).map(m -> m.mask)
                      .reduce((r1, r2) -> r1 | r2)
                      .orElse(0));
    }

    public boolean matches(int mask) {
        return (this.mask | mask) != 0;
    }

    public static HttpMethodMask any() {
        return new HttpMethodMask(ANY_METHOD_MASK);
    }

    public static HttpMethodMask modifying() {
        return combine(POST, DELETE, PUT);
    }

    public static class HttpMethodMask {
        private int mask;

        private HttpMethodMask(int mask) {
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }
    }
}
