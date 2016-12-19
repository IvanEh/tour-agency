package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public final class RandomHotTourController extends Controller {
    private TourDao tourDao = ServiceLocator.INSTANCE.get(TourDao.class);

    private final int TRIALS_LIMIT = 5;
    private final int COUNT = 3;
    private final Comparator<Tour> tourComparator =
            (t1, t2) -> (int) Math.signum(t1.getId() - t2.getId());

    @Override
    public void get(RequestService reqService) {
        Set<Tour> randomTours = new TreeSet<>(tourComparator);
        int trials = 0;

        while (randomTours.size() < COUNT && trials <= TRIALS_LIMIT) {
            Tour tour = tourDao.findRandomHot();

            if(tour != null) {
                int currSize = randomTours.size();
                randomTours.add(tour);

                if(randomTours.size() == currSize) {
                    trials++;
                } else {
                    trials = 0;
                }
            } else {
                trials++;
            }
        }

        reqService.putParameter("tours", randomTours);
    }
}
