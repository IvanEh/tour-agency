package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;

public class RedirectController extends Controller {
    private final String path;

    public RedirectController(String path) {
        this.path = path;
    }

    @Override
    public void any(RequestService reqService) {
        reqService.renderPage(path);
    }
}
