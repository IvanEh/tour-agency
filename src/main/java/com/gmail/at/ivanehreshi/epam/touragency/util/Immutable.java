package com.gmail.at.ivanehreshi.epam.touragency.util;

import java.util.*;

public class Immutable {
    public static <E> List<E> cons(Iterable<E>... iters) {
        List<E> list = new ArrayList<E>();
        for(Iterable<E> iter: iters) {
            if(iter != null) {
                for (E e1 : iter) list.add(e1);
            }
        }
        return list;
    }
}
