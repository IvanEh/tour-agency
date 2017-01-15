package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public class AgentEditTourController extends Controller {

    private TourService tourService = ServiceLocator.INSTANCE.get(TourService.class);

    @Override
    public void get(RequestService reqService) {
        Long id = reqService.getLong("id").orElse(null);
        reqService.putParameter("tour", tourService.read(id));
    }
}
