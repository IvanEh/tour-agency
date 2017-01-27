package com.gmail.at.ivanehreshi.epam.touragency.service.impl;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;

import java.math.*;
import java.util.*;

public class TourServiceImpl extends AbstractDaoService<Tour, Long>
        implements TourService {

    private final int TRIALS_LIMIT = 5;

    private final Comparator<Tour> tourComparator =
            (t1, t2) -> (int) Math.signum(t1.getId() - t2.getId());

    private TourDao tourDao;

    public TourServiceImpl(TourDao tourDao) {
        this.tourDao = tourDao;
    }

    @Override
    public void create(Tour tour) {
        Long id = tourDao.create(tour);
        tour.setId(id);
    }

    @Override
    public Set<Tour> findRandomHotTours(int count) {
        Set<Tour> randomTours = new TreeSet<>(tourComparator);
        int trials = 0;

        while (randomTours.size() < count && trials <= TRIALS_LIMIT) {
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

        return randomTours;
    }

    @Override
    public void toggleEnabled(Long id) {
        Tour tour = tourDao.read(id);
        tour.setEnabled(!tour.isEnabled());
        tourDao.update(tour);
    }

    @Override
    public BigDecimal computePrice(Long tourId, Long userId) {
        return tourDao.computePrice(tourId, userId);
    }

    @Override
    public List<Tour> executeDynamicFilter(ToursDynamicFilter filter) {
        return tourDao.executeDynamicFilter(filter);
    }

    @Override
    public TourDao getDao() {
        return tourDao;
    }
}
