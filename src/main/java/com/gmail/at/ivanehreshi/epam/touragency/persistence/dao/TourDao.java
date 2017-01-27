package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;

import java.math.*;
import java.util.*;

public interface TourDao extends Dao<Tour, Long> {
    BigDecimal computePrice(Long tourId, Long userId);

    Tour findRandomHot();

    List<Tour> executeDynamicFilter(ToursDynamicFilter filter);
}
