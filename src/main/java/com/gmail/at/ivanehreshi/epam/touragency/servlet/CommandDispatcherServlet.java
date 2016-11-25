package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import com.gmail.at.ivanehreshi.epam.touragency.command.Command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandDispatcherServlet extends HttpServlet {
    private List<MatcherEntry> postMatchers;

    CommandDispatcherServlet(List<MatcherEntry> postMatchers) {
        this.postMatchers = postMatchers;
    }

    public void addMapping(String regex, List<Command> commands) {
        MatcherEntry matcherEntry = new MatcherEntry(regex, commands);
        postMatchers.add(matcherEntry);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if(pathInfo == null)
            pathInfo = "/";

        for(MatcherEntry matcherEntry: postMatchers) {
            matcherEntry.matcher.reset(pathInfo);
            if(matcherEntry.matcher.matches()) {
                resp.setStatus(HttpServletResponse.SC_OK);

                List<String> groups = new ArrayList<>(matcherEntry.matcher.groupCount());

                for (int i = 0; i < matcherEntry.matcher.groupCount(); i++) {
                    groups.add(matcherEntry.matcher.group(i + 1));
                }

                matcherEntry.call(req, resp, groups);
                break;
            }
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
