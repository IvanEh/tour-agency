package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;

public class AgentNewTourPageController extends Controller {
    @Override
    public void get(RequestService reqService) {
        Tour tour = (Tour) reqService.getFlashParameter("tour");
        reqService.setPageAttribute("tour", tour);
    }
}
