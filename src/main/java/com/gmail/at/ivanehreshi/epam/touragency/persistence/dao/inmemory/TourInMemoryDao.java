package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.inmemory;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;

public class TourInMemoryDao extends AbstractInMemoryDao<Tour> implements TourDao{
    public TourInMemoryDao() {
        super(Tour.class);
    }

    @Override
    public Long create(Tour tour) {
        Long key = super.create(tour);
        tour.setId(key);
        return key;
    }

    @Override
    public void update(Tour tour) {
        getEntities().replace(tour.getId(), tour);
    }
}
