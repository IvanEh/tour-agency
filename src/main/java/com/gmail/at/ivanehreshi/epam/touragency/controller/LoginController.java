package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;

import javax.servlet.ServletException;

public final class LoginController extends Controller {

    @Override
    public void post(RequestService reqService) {
        String username = reqService.getString("username");
        String password = reqService.getString("password");

        try {
            System.out.print("username: ");
            System.out.println(username);

            reqService.getRequest().login(username, password);

            reqService.redirect("/tours.html");
        } catch (ServletException e) {
            reqService.redirect("/login.html?failed=true");
        }
    }
}
