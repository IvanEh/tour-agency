package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;

import java.util.*;

public interface ReviewDao extends Dao<Review, Long> {

    List<Review> findByTour(Long id);

    boolean canVote(Long userId, Long tourId);
}
