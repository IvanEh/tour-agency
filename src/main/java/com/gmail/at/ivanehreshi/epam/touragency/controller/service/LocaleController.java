package com.gmail.at.ivanehreshi.epam.touragency.controller.service;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;

public class LocaleController extends Controller {
    @Override
    public void get(RequestService reqService) {
        String lang = reqService.getString("lang");

        if(lang != null && !lang.isEmpty()) {
            reqService.getRequest().getSession().setAttribute("lang", lang);
        } else {
            lang = (String) reqService.getRequest().getSession(false).getAttribute("lang");
            if(lang == null) {
                lang = "en";
            }
        }

        reqService.putParameter("locale", lang + "_" + lang.toUpperCase());

    }

    @Override
    public boolean isService() {
        return true;
    }
}
