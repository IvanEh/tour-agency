package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import com.gmail.at.ivanehreshi.epam.touragency.command.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.filter.StaticResourceFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CommandDispatcherServletBuilder {
    private final ServletContext servletContext;

    private List<CommandDispatcherServlet.MatcherEntry> matchers
                = new ArrayList<>();

    public CommandDispatcherServletBuilder(ServletContext sc) {
        this.servletContext = sc;
    }

    public CommandDispatcherServletBuilder addMapping(String regex, int mask, Controller controller) {
        CommandDispatcherServlet.MatcherEntry matcherEntry =
                new CommandDispatcherServlet.MatcherEntry(regex, mask, controller);
        matchers.add(matcherEntry);

        return this;
    }

    public CommandDispatcherServletBuilder addMapping(String regex, Controller controller) {
        return addMapping(regex, HttpMethod.ANY_METHOD_MASK, controller);
    }

    public CommandDispatcherServletBuilder reset() {
        matchers.clear();
        return this;
    }

    public CommandDispatcherServlet build() {
        CommandDispatcherServlet dispatcherServlet = new CommandDispatcherServlet();

        for (CommandDispatcherServlet.MatcherEntry entry: matchers) {
            dispatcherServlet.addMapping(entry);
        }

        return dispatcherServlet;
    }

    public CommandDispatcherServlet buildAndRegister(String name, String mapping) {
        CommandDispatcherServlet servlet = build();

        ServletRegistration.Dynamic dynamic = servletContext.addServlet(name, servlet);
        dynamic.addMapping(mapping);

        FilterRegistration.Dynamic filterDynamic =
                servletContext.addFilter("Static Resource Filter", new StaticResourceFilter(mapping.replace("/*", "")));
        filterDynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");

        return servlet;
    }

}
