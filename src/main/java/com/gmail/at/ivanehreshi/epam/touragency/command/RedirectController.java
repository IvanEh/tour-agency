package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;

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
