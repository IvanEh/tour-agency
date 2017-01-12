package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;

import javax.servlet.*;

public final class LoginController extends Controller {

    @Override
    public void post(RequestService reqService) {
        String username = reqService.getString("username");
        String password = reqService.getString("password");

        try {
            reqService.getRequest().login(username, password);
            reqService.redirect("/tours.html");
        } catch (ServletException e) {
            reqService.redirect("/login.html?failed=true");
        }
    }
}
