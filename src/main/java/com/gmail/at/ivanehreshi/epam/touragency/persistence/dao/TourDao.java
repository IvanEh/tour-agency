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

    /**
     * Allows retrieving paged tours in an efficient manner
     * Sorts tours by price and filters them by tour type
     *
     * @param count  - number of rows to be retrieved
     * @param anchor - tour which will helps determine the next or previous page
     * @param dir    - direction into which the 'slice' will 'move'
     * @param priceOrdering - sorting tours by price
     * @param types - TourTypes to be included. If null then return all types
     * @return a 'slice' of data from the database
     */
    Slice<Tour> getToursSliceByCriteria(int count, Tour anchor, ScrollDirection dir,
                                        Ordering priceOrdering, TourType... types);

    Tour findRandomHot();
}
