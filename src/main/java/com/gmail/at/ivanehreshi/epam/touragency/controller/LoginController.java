package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public final class LoginController extends Controller {

    private AuthService authService = ServiceLocator.INSTANCE.get(AuthService.class);

    @Override
    public void post(RequestService reqService) {
        String username = reqService.getString("username");
        String password = reqService.getString("password");

        if (authService.login(reqService.getRequest(), username, password)) {
            reqService.redirect("/tours.html");
        } else {
            reqService.redirect("/login.html?failed=true");
        }
    }
}
