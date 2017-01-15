package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.apache.logging.log4j.*;

public final class LogoutController extends Controller {

    private static final Logger LOGGER = LogManager.getLogger(LogoutController.class);

    private AuthService authService = ServiceLocator.INSTANCE.get(AuthService.class);

    @Override
    public void post(RequestService reqService) {
        authService.logout(reqService.getRequest());
        reqService.redirect("/login.html");
    }
}
