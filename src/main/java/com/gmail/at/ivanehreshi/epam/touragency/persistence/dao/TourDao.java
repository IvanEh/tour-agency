package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;

import java.math.BigDecimal;

public interface TourDao extends Dao<Tour, Long> {
    BigDecimal computePrice(Long tourId, Long userId);
}
