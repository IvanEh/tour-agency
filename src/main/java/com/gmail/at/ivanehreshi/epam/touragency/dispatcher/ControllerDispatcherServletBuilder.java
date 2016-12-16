package com.gmail.at.ivanehreshi.epam.touragency.dispatcher;

import com.gmail.at.ivanehreshi.epam.touragency.filter.StaticResourceFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ControllerDispatcherServletBuilder {
    private final ServletContext servletContext;

    private List<ControllerDispatcherServlet.MatcherEntry> matchers
                = new ArrayList<>();

    public ControllerDispatcherServletBuilder(ServletContext sc) {
        this.servletContext = sc;
    }

    public ControllerDispatcherServletBuilder addMapping(String regex, int mask, Controller controller) {
        ControllerDispatcherServlet.MatcherEntry matcherEntry =
                new ControllerDispatcherServlet.MatcherEntry(regex, mask, controller);
        matchers.add(matcherEntry);

        return this;
    }

    public ControllerDispatcherServletBuilder addMapping(String regex, Controller controller) {
        return addMapping(regex, HttpMethod.ANY_METHOD_MASK, controller);
    }

    public ControllerDispatcherServletBuilder reset() {
        matchers.clear();
        return this;
    }

    public ControllerDispatcherServlet build() {
        ControllerDispatcherServlet dispatcherServlet = new ControllerDispatcherServlet();

        for (ControllerDispatcherServlet.MatcherEntry entry: matchers) {
            dispatcherServlet.addMapping(entry);
        }

        return dispatcherServlet;
    }

    public ControllerDispatcherServlet buildAndRegister(String name, String mapping) {
        ControllerDispatcherServlet servlet = build();

        ServletRegistration.Dynamic dynamic = servletContext.addServlet(name, servlet);
        dynamic.addMapping(mapping);

        FilterRegistration.Dynamic filterDynamic =
                servletContext.addFilter("Static Resource Filter", new StaticResourceFilter(mapping.replace("/*", "")));
        filterDynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");

        return servlet;
    }

}
