package com.gmail.at.ivanehreshi.epam.touragency.servlet;

import com.gmail.at.ivanehreshi.epam.touragency.command.Controller;

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
    private List<MatcherEntry> httpMatchers;
    private List<MatcherEntry> httpServiceMatchers;


    public CommandDispatcherServlet() {
        httpMatchers = new ArrayList<>();
        httpServiceMatchers = new ArrayList<>();
    }


    public void addMapping(String regex, Controller controller) {
        MatcherEntry matcherEntry = new MatcherEntry(regex, controller);
        addMapping(matcherEntry);
    }

    public void addMapping(MatcherEntry entry) {
        if(entry.controller.isService()) {
            httpMatchers.add(entry);
        } else {
            httpServiceMatchers.add(entry);
        }
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

        RequestService requestService = new RequestService(req, resp, null);

        dispatchLoop(req, resp, pathInfo, requestService, httpMatchers, true);
        dispatchLoop(req, resp, pathInfo, requestService, httpServiceMatchers, false);

        if (requestService.getRedirectPath() != null) {
            try {
                resp.sendRedirect(requestService.getRedirectPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestService.getRenderPage() != null) {
            try {
                req.getRequestDispatcher(requestService.getRenderPage()).forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void dispatchLoop(HttpServletRequest req, HttpServletResponse resp, String pathInfo,
                              RequestService requestService,
                              List<MatcherEntry> httpMatchers, boolean matchFirst) {

        for(MatcherEntry matcherEntry: httpMatchers) {
            matcherEntry.matcher.reset(pathInfo);

            if(matcherEntry.matcher.matches()) {
                resp.setStatus(HttpServletResponse.SC_OK);

                List<String> groups = new ArrayList<>(matcherEntry.matcher.groupCount());

                requestService.setGroups(groups);

                for (int i = 0; i < matcherEntry.matcher.groupCount(); i++) {
                    groups.add(matcherEntry.matcher.group(i + 1));
                }

                matcherEntry.call(requestService);

                if(matchFirst) {
                    break;
                }
            }
        }
    }


    static class MatcherEntry {
        private final Matcher matcher;

        private final Controller controller;

        public MatcherEntry(String regex, Controller controller) {
            Pattern p = Pattern.compile("^" + regex + "$");
            matcher = p.matcher("");
            this.controller = controller;
        }

        public void call(RequestService requestService) {
            controller.execute(requestService);
        }
    }
}
