package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public final class LoginController extends Controller {

    private AuthService authService = ServiceLocator.INSTANCE.get(AuthService.class);

    @Override
    public void get(RequestService reqService) {
        String username = (String) reqService.getFlashParameter("username");
        reqService.putParameter("username", username);
    }

    @Override
    public void post(RequestService reqService) {
        String username = reqService.getString("username");
        String password = reqService.getString("password");
        String destination = reqService.getRequest().getHeader("Referer");

        if (authService.login(reqService.getRequest(), username, password)) {
            if(destination.contains("login")) {
                reqService.redirect("/");
            } else {
                reqService.redirect(destination);
            }
        } else {
            reqService.redirect("/login.html?failed=true");
            reqService.putFlashParameter("username", username);
        }
    }
}
