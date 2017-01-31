package com.gmail.at.ivanehreshi.epam.touragency.persistence.transaction;

import java.lang.reflect.*;
import java.util.*;

public class Annotated {
    private Map<Class<?>, List<Method>> cache;

    public void m(Object o) {
        Class<?> clazz = o.getClass();
        Method[] methods = clazz.getMethods();
    }
}
