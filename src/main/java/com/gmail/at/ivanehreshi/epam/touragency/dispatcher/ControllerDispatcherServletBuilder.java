package com.gmail.at.ivanehreshi.epam.touragency.dispatcher;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.filter.*;
import com.gmail.at.ivanehreshi.epam.touragency.security.*;

import javax.servlet.*;
import java.util.*;

/**
 * A more user friendly way of creating a registering a {@link ControllerDispatcherServlet}
 */
public class ControllerDispatcherServletBuilder {
    private final ServletContext servletContext;

    private List<ControllerDispatcherServlet.MatcherEntry> matchers
            = new ArrayList<>();

    public ControllerDispatcherServletBuilder(ServletContext sc) {
        this.servletContext = sc;
    }

    public ControllerDispatcherServletBuilder addMapping(String regex,
                  HttpMethod.HttpMethodMask mask, Controller controller, Role... roles) {

        ControllerDispatcherServlet.MatcherEntry matcherEntry =
                new ControllerDispatcherServlet.MatcherEntry(regex, mask.getMask(), controller);

        if(roles.length != 0) {
            SecurityContext.INSTANCE.addSecurityConstraint(regex, mask, roles);
        }

        matchers.add(matcherEntry);

        return this;
    }

    public ControllerDispatcherServletBuilder addMapping(String regex, Controller controller) {
        return addMapping(regex, HttpMethod.any(), controller);
    }

    public ControllerDispatcherServletBuilder reset() {
        matchers.clear();
        return this;
    }

    public ControllerDispatcherServlet build() {
        ControllerDispatcherServlet dispatcherServlet = new ControllerDispatcherServlet();

        for (ControllerDispatcherServlet.MatcherEntry entry : matchers) {
            dispatcherServlet.addMapping(entry);
        }

        return dispatcherServlet;
    }

    public ControllerDispatcherServlet buildAndRegister(String name, String mapping) {
        ControllerDispatcherServlet servlet = build();

        ServletRegistration.Dynamic dynamic = servletContext.addServlet(name, servlet);
        dynamic.setMultipartConfig(new MultipartConfigElement(""));
        dynamic.addMapping(mapping);

        StaticResourceFilter filter =
                new StaticResourceFilter(mapping.replace("/*", ""));
        filter.ignore("/image-provider");

        FilterRegistration.Dynamic filterDynamic =
                servletContext.addFilter("Static Resource Filter", filter);
        filterDynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");

        return servlet;
    }

}
