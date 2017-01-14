package com.gmail.at.ivanehreshi.epam.touragency.service.impl;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;

public class TourServiceImpl extends AbstractDaoService<Tour, Long>
        implements TourService {

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
    public TourDao getDao() {
        return tourDao;
    }
}
