package com.gmail.at.ivanehreshi.epam.touragency.service.impl;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;

import java.util.*;

public class TourImageServiceImpl extends AbstractDaoService<TourImage, Long>
        implements TourImageService  {
    private TourImageDao tourImageDao;

    public TourImageServiceImpl(TourImageDao tourImageDao) {
        this.tourImageDao = tourImageDao;
    }

    @Override
    public void create(TourImage tourImage) {
        Long id = tourImageDao.create(tourImage);
        tourImage.setId(id);
    }

    // backing dao
    @Override
    public TourImageDao getBackingDao() {
        return tourImageDao;
    }

    @Override
    public List<TourImage> findByTour(long tourId) {
        return tourImageDao.findByTour(tourId);
    }
}
