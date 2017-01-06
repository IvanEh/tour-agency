package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;

public class AgentNewTourPageController extends Controller {
    @Override
    public void get(RequestService reqService) {
        Tour tour = (Tour) reqService.getFlashParameter("tour");
        reqService.putParameter("tour", tour);
    }
}
