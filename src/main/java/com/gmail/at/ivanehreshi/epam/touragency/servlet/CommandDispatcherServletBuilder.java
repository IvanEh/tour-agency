package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import com.gmail.at.ivanehreshi.epam.touragency.command.Command;
import com.gmail.at.ivanehreshi.epam.touragency.web.HttpMethod;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandDispatcherServletBuilder {
    private final ServletContext servletContext;

    private List<CommandDispatcherServlet.MatcherEntry> postMatchers
                = new ArrayList<>();
    private List<CommandDispatcherServlet.MatcherEntry> getMatchers
            = new ArrayList<>();
    private List<CommandDispatcherServlet.MatcherEntry> deleteMatchers
            = new ArrayList<>();

    public CommandDispatcherServletBuilder(ServletContext sc) {
        this.servletContext = sc;
    }

    public CommandDispatcherServletBuilder addMapping(HttpMethod method, String regex, Command command) {
        CommandDispatcherServlet.MatcherEntry matcherEntry =
                new CommandDispatcherServlet.MatcherEntry(regex, Arrays.asList(command));

        switch (method) {
            case POST:
                postMatchers.add(matcherEntry);
                break;
            case GET:
                getMatchers.add(matcherEntry);
                break;
            case DELETE:
                deleteMatchers.add(matcherEntry);
                break;
            default:
                break;
        }

        return this;
    }

    public CommandDispatcherServletBuilder mapPost(String regex, Command command) {
        addMapping(HttpMethod.POST, regex, command);
        return this;
    }

    public CommandDispatcherServletBuilder mapGet(String regex, Command command) {
        addMapping(HttpMethod.GET, regex, command);
        return this;
    }

    public CommandDispatcherServletBuilder mapDelete(String regex, Command command) {
        addMapping(HttpMethod.DELETE, regex, command);
        return this;
    }

    public CommandDispatcherServletBuilder reset() {
        postMatchers.clear();
        return this;
    }

    public CommandDispatcherServlet build() {
        CommandDispatcherServlet dispatcherServlet = new CommandDispatcherServlet();
        dispatcherServlet.addMappings(HttpMethod.POST, postMatchers);
        dispatcherServlet.addMappings(HttpMethod.GET, getMatchers);
        dispatcherServlet.addMappings(HttpMethod.DELETE, deleteMatchers);
        return dispatcherServlet;
    }

    public CommandDispatcherServlet buildAndRegister(String name, String mapping) {
        CommandDispatcherServlet servlet = build();
        ServletRegistration.Dynamic dynamic = servletContext.addServlet(name, servlet);
        dynamic.addMapping(mapping);
        return servlet;
    }

}
