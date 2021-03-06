package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;

import java.math.*;
import java.util.*;

public interface TourService extends CrudService<Tour, Long> {

    Set<Tour> findRandomHotTours(int count);

    void toggleEnabled(Long id);

    BigDecimal computePrice(Long tourId, Long userId);

    List<Tour> executeDynamicFilter(ToursDynamicFilter filter);
}
