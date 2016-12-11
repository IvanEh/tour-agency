package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import com.gmail.at.ivanehreshi.epam.touragency.command.Command;
import com.gmail.at.ivanehreshi.epam.touragency.web.HttpMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandDispatcherServlet extends HttpServlet {
    private EnumMap<HttpMethod, List<MatcherEntry>> httpMatchers;


    public CommandDispatcherServlet() {
        httpMatchers = new EnumMap<>(HttpMethod.class);
        initHttpMap();
    }


    public void addMapping(HttpMethod method, String regex, List<Command> commands) {
        MatcherEntry matcherEntry = new MatcherEntry(regex, commands);
        httpMatchers.get(method).add(matcherEntry);
    }

    public void addPostMapping(String regex, List<Command> commands) {
        addMapping(HttpMethod.POST, regex, commands);
    }

    public void addGetMapping(String regex, List<Command> commands) {
        addMapping(HttpMethod.GET, regex, commands);
    }

    public void addDeleteMapping(String regex, List<Command> commands) {
        addMapping(HttpMethod.DELETE, regex, commands);
    }

    void addMappings(HttpMethod method, List<MatcherEntry> entries) {
        httpMatchers.get(method).addAll(entries);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null)
            pathInfo = "/";

        boolean nonServiceFired = false;
        
        for(MatcherEntry matcherEntry: httpMatchers.get(HttpMethod.valueOf(req.getMethod()))) {
            matcherEntry.matcher.reset(pathInfo);
        
            if(nonServiceFired && matcherEntry.commands.get(0).isService()) {
                continue;
            }
            
            if(matcherEntry.matcher.matches()) {
                resp.setStatus(HttpServletResponse.SC_OK);

                List<String> groups = new ArrayList<>(matcherEntry.matcher.groupCount());

                for (int i = 0; i < matcherEntry.matcher.groupCount(); i++) {
                    groups.add(matcherEntry.matcher.group(i + 1));
                }
                
                if(!matcherEntry.commands.get(0).isService()) {
                    nonServiceFired = true;
                }
                
                matcherEntry.call(req, resp, groups);
            }
        }
    }

    private void initHttpMap() {
        for (HttpMethod m : HttpMethod.values()) {
            httpMatchers.put(m, new ArrayList<>());
        }
    }

    static class MatcherEntry {
        private Matcher matcher;

        private List<Command> commands = new ArrayList<>();

        public MatcherEntry(String regex, List<Command> commands) {
            Pattern p = Pattern.compile("^" + regex + "$");
            matcher = p.matcher("");
            this.commands.addAll(commands);
        }

        public void addCommand(Command command) {
            commands.add(command);
        }

        public void call(HttpServletRequest req, HttpServletResponse resp, List<String> groups) {
            commands.forEach((command) -> command.execute(req, resp, groups));
        }
    }
}
