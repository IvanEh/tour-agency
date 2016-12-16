package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;

import javax.servlet.ServletException;

public class LogoutController extends Controller {

    @Override
    public void post(RequestService reqService) {
        try {
            reqService.getRequest().logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        reqService.redirect("/login.html");

    }
}
