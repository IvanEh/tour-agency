package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import com.gmail.at.ivanehreshi.epam.touragency.command.Controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.List;

public class CommandDispatcherServletBuilder {
    private final ServletContext servletContext;

    private List<CommandDispatcherServlet.MatcherEntry> matchers
                = new ArrayList<>();

    public CommandDispatcherServletBuilder(ServletContext sc) {
        this.servletContext = sc;
    }

    public CommandDispatcherServletBuilder addMapping(String regex, Controller controller) {
        CommandDispatcherServlet.MatcherEntry matcherEntry =
                new CommandDispatcherServlet.MatcherEntry(regex, controller);
        matchers.add(matcherEntry);

        return this;
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
        return servlet;
    }

}
