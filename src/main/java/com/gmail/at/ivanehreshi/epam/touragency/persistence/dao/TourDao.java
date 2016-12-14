package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.util.Ordering;

import java.math.BigDecimal;
import java.util.List;

public interface TourDao extends Dao<Tour, Long> {
    BigDecimal computePrice(Long tourId, Long userId);

    List<Tour> findByCriteria(Ordering priceOrdering, TourType... types);
}
