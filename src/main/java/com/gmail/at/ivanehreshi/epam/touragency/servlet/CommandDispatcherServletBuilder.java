package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import com.gmail.at.ivanehreshi.epam.touragency.command.Command;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandDispatcherServletBuilder {
    private final ServletContext servletContext;

    private List<CommandDispatcherServlet.MatcherEntry> postMatchers
                = new ArrayList<>();

    public CommandDispatcherServletBuilder(ServletContext sc) {
        this.servletContext = sc;
    }

    public CommandDispatcherServletBuilder addMapping(String regex, Command command) {
        CommandDispatcherServlet.MatcherEntry matcherEntry =
                new CommandDispatcherServlet.MatcherEntry(regex, Arrays.asList(command));

        postMatchers.add(matcherEntry);
        return this;
    }

    public CommandDispatcherServletBuilder reset() {
        postMatchers.clear();
        return this;
    }

    public CommandDispatcherServlet build() {
        return new CommandDispatcherServlet(new ArrayList<>(postMatchers));
    }

    public CommandDispatcherServlet buildAndRegister(String name, String mapping) {
        CommandDispatcherServlet servlet = build();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(name, servlet);
        dynamic.addMapping(mapping);
        return servlet;
    }

}
