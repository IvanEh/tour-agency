package com.gmail.at.ivanehreshi.epam.touragency.web;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ServiceLocator {
    INSTANCE;

    private Map<String, Object> objects = new ConcurrentHashMap<>();
    private ServletContext servletContext;

    public void publish(String name, Object o, boolean elScope) {
        objects.put(name, o);
        if(elScope) {
            servletContext.setAttribute(name, o);
        }
    }

    public <T> void publish(Class<T> clazz) {
        try {
            Object o = clazz.newInstance();
            publish(o);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void publish(Object o) {
        publish(o, true, true);
        publish(o, false, true);
    }

    public void publish(Object o, boolean shortName, boolean elScope) {
        if(shortName) {
            publish(startWithLower(o.getClass().getSimpleName()), o, elScope);
        } else {
            publish(o.getClass().getName(), o, elScope);
        }
    }

    public <T> void publish(Class<T> clazz, T t) {
        publish(startWithLower(clazz.getSimpleName()), t, true);
        publish(clazz.getName(), t, true);
    }

    public Object remove(String name) {
        Object o = objects.remove(name);

        servletContext.setAttribute(name, null);
        return o;
    }

    public <T> T remove(Class<T> clazz) {
        Object o1 = remove(clazz.getName());

        Object o2 = remove(startWithLower(clazz.getSimpleName()));

        return (T) (o1 == null ? o2 : o1);
    }

    public Object get(String name) {
        return objects.get(name);
    }

    public <T> T get(String name, Class<T> clazz) {
        Object o = get(name);

        if(clazz.isInstance(o)) {
            return (T) o;
        }

        return null;
    }

    public <T> T get(Class<T> clazz) {
        T t = get(startWithLower(clazz.getSimpleName()), clazz);

        if(t == null) {
            t = get(clazz.getName(), clazz);
        }

        return t;
    }

    private String startWithLower(String s) {
        StringBuilder name = new StringBuilder(s);
        name.replace(0, 1, String.valueOf(Character.toLowerCase(name.charAt(0))));
        return name.toString();
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
