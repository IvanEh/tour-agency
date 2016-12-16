package com.gmail.at.ivanehreshi.epam.touragency.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allows sharing objects between different application modules
 * Also contains methods to make objects available directly in
 * the JSP page
 *
 * <p>Two main methods user get() and publish() that have various overloaded versions
 *
 * <p>Shortname of an object - its name in source file with a lower case first letter
 */
public enum ServiceLocator {
    INSTANCE;

    public static final Logger LOGGER = LogManager.getLogger(ServiceLocator.class);

    private Map<String, Object> objects = new ConcurrentHashMap<>();

    /**
     * The field used for passing object to JSP pages
     */
    private ServletContext servletContext;

    /**
     * Make object globally available by the given name
     * @param name    name by which the object can be retrieved
     * @param o       the shared object
     * @param elScope if true - publishes the object on all JSP pages
     */
    public void publish(String name, Object o, boolean elScope) {
        objects.put(name, o);
        if(elScope) {
            servletContext.setAttribute(name, o);
        }
    }

    /**
     * Create an instance of the given class and publishes it by
     * its fully qualified name and short name
     */
    public <T> void publish(Class<T> clazz) {
        try {
            Object o = clazz.newInstance();
            publish(o);
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Cannot publish object", e);
        }
    }

    /**
     * publishes the object by its class fully qualified and short name
     */
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

    /**
     * Get published object by name
     */
    public Object get(String name) {
        return objects.get(name);
    }

    /**
     * Get published object by name and immediately cast it to the
     * given type
     */
    public <T> T get(String name, Class<T> clazz) {
        Object o = get(name);

        if(clazz.isInstance(o)) {
            return (T) o;
        }

        return null;
    }

    /**
     * Tries to find the object by its fully qualified and short name
     */
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
