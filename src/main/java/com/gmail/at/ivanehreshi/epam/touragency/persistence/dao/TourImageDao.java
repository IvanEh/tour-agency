package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;

import java.util.*;

public interface TourImageDao extends Dao<TourImage, Long> {
    List<TourImage> findByTour(Long tourId);
}
