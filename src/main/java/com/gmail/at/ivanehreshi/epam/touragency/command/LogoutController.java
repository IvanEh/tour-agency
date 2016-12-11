package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;

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
