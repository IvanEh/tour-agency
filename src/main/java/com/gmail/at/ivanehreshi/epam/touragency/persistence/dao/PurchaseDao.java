package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;

import java.util.*;

public interface PurchaseDao extends Dao<Purchase,Long> {
    List<Purchase> findByUser(Long userId);

    List<Purchase> findByUserTour(Long userId, Long tourId);

    List<Purchase> findByStatusOrderByDate(PurchaseStatus active);
}
