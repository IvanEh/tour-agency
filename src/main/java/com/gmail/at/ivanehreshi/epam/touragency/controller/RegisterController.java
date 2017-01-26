package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public final class RegisterController extends Controller {

    private AuthService authService = ServiceLocator.INSTANCE.get(AuthService.class);

    @Override
    public void get(RequestService reqService) {
        User user = (User) reqService.getFlashParameter("user");
        reqService.putParameter("user", user);
        reqService.putParameter("error", reqService.getFlashParameter("error"));
    }

    @Override
    public void post(RequestService reqService) {
        User user = new User();

        user.setUsername(reqService.getString("username"));
        user.setFirstName(reqService.getString("firstName"));
        user.setLastName(reqService.getString("lastName"));

        String pass = reqService.getString("password");

        if (pass.length() < 7) {
            user.setPassword(null);
            reqService.putFlashParameter("user", user);
            reqService.putFlashParameter("error", "register.error.password");

            reqService.redirect("/register.html");
            return;
        }

        if(!authService.register(user, pass)) {
            reqService.putFlashParameter("error", "register.error.username");

            reqService.redirect("/register.html");
            return;
        }

        reqService.redirect("/login.html");
    }
}
