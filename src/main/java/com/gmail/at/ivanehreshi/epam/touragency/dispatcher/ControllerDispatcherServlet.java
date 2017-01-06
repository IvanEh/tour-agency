package com.gmail.at.ivanehreshi.epam.touragency.dispatcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dispatches incoming request to mapped Controllers
 */
public class ControllerDispatcherServlet extends HttpServlet {
    public static final Logger LOGGER = LogManager.getLogger(ControllerDispatcherServlet.class);

    public static final String FLASH_SESSION_KEY = "__flash";
    public static final String REDIRECT_KEY = "__redirect";

    private final List<MatcherEntry> httpMatchers;

    private final List<MatcherEntry> httpServiceMatchers;


    public ControllerDispatcherServlet() {
        httpMatchers = new ArrayList<>();
        httpServiceMatchers = new ArrayList<>();
    }

    /**
     * Define a URL-Controller mapping
     *
     * @param regex      - regular expression that is used for matching
     * @param mask       - request method mask(e.g HttpMethod.GET.mask & HttpMethod.PUT.mask)
     * @param controller
     */
    public void addMapping(String regex, int mask, Controller controller) {
        MatcherEntry matcherEntry = new MatcherEntry(regex, mask, controller);
        addMapping(matcherEntry);
    }

    void addMapping(MatcherEntry entry) {
        if (entry.controller.isService()) {
            httpServiceMatchers.add(entry);
        } else {
            httpMatchers.add(entry);
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
        if (pathInfo == null)
            pathInfo = "/";

        RequestService requestService = new RequestService(req, resp, null);

        boolean any = false;
        any |= dispatchLoop(req, resp, pathInfo, requestService, httpMatchers, true);
        any |= dispatchLoop(req, resp, pathInfo, requestService, httpServiceMatchers, false);


        tryRedirect(resp, requestService);

        tryRender(req, resp, requestService, any);

        if(!requestService.isRedirect()) {
            requestService.clearFlash();
        } else {
            requestService.clearRedirectFlag();
        }
    }

    private void tryRender(HttpServletRequest req, HttpServletResponse resp, RequestService requestService, boolean any) {
        if (requestService.getRenderPage() != null) {
            try {
                req.getRequestDispatcher(requestService.getRenderPage()).forward(req, resp);
            } catch (ServletException | IOException e) {
                LOGGER.warn("An exception happened at page rendering phase");
            }
        } else if (!any) {
            try {
                req.getRequestDispatcher("/pages/__error").forward(req, resp);
            } catch (ServletException | IOException e) {
                LOGGER.warn("An exception happened at page rendering phase");
            }
        }
    }

    private void tryRedirect(HttpServletResponse resp, RequestService requestService) {
        if (requestService.getRedirectPath() != null) {
            try {
                requestService.getRequest().getSession().setAttribute(REDIRECT_KEY, true);
                resp.sendRedirect(requestService.getRedirectPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean dispatchLoop(HttpServletRequest req, HttpServletResponse resp, String pathInfo,
                                 RequestService requestService,
                                 List<MatcherEntry> httpMatchers, boolean matchFirst) {
        boolean any = false;
        HttpMethod method = requestService.getMethod();

        for (MatcherEntry matcherEntry : httpMatchers) {
            matcherEntry.matcher.reset(pathInfo);

            if ((matcherEntry.mask & method.mask) == 0) {
                continue;
            }

            if (matcherEntry.matcher.matches()) {
                resp.setStatus(HttpServletResponse.SC_OK);

                any = true;

                matcherEntry.call(requestService);

                if (matchFirst) {
                    break;
                }
            }
        }

        return any;
    }


    /**
     * This class represent a URL pattern - Controller pair
     */
    static class MatcherEntry {
        private final Matcher matcher;

        private int mask;

        private final Controller controller;

        public MatcherEntry(String regex, int mask, Controller controller) {
            Pattern p = Pattern.compile("^" + regex + "$");
            matcher = p.matcher("");
            this.controller = controller;
            this.mask = mask;
        }

        public void call(RequestService requestService) {
            controller.execute(requestService);
        }
    }
}
