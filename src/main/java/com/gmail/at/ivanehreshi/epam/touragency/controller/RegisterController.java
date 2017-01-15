package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public final class RegisterController extends Controller {

    private AuthService authService = ServiceLocator.INSTANCE.get(AuthService.class);

    @Override
    public void post(RequestService reqService) {
        User user = new User();

        user.setUsername(reqService.getString("username"));
        user.setFirstName(reqService.getString("firstName"));
        user.setLastName(reqService.getString("lastName"));

        String pass = reqService.getString("password");

        authService.register(user, pass);

        reqService.redirect("/login.html");
    }
}
