package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public final class RandomHotTourController extends Controller {

    private TourService tourService = ServiceLocator.INSTANCE.get(TourService.class);

    private final int COUNT = 5;

    @Override
    public void get(RequestService reqService) {
        Set<Tour> randomTours = tourService.findRandomHotTours(COUNT);
        reqService.setPageAttribute("tours", randomTours);
    }
}
