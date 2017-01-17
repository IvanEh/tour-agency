package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;

import java.util.*;

public interface ReviewService extends CrudService<Review, Long> {
    List<Review> findByTour(Long id);

    boolean canVote(Long userId, Long tourId);
}
