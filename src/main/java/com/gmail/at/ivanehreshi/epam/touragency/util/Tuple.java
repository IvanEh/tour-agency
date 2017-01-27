package com.gmail.at.ivanehreshi.epam.touragency.util;

public class Tuple<K, V> {
    private K first;
    private V second;

    public Tuple(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }
}
