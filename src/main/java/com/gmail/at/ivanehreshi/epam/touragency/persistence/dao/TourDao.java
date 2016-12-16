package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.Dao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ScrollDirection;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.Slice;
import com.gmail.at.ivanehreshi.epam.touragency.util.Ordering;

import java.math.BigDecimal;

public interface TourDao extends Dao<Tour, Long> {
    BigDecimal computePrice(Long tourId, Long userId);

    Slice<Tour> getToursSliceByCriteria(int count, Tour anchor, ScrollDirection dir,
                                        Ordering priceOrdering, TourType... types);
}
