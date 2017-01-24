package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;

import java.util.*;

public interface TourImageService extends CrudService<TourImage, Long> {
    List<TourImage> findByTour(long tourId);
}
