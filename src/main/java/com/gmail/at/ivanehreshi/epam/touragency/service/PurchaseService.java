package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public interface PurchaseService extends CrudService<Purchase, Long> {
    List<Purchase> findByUser(Long id);

    Purchase purchase(Long userId, Long tourId);

    List<Purchase> findByUserTour(Long userId, Long tourId);

    List<Group<Tour, Purchase>> findByUserGroupByTourOrdered(Long id);

    void acknowledge(Long purchaseId);

    void cancel(Long purchaseId);

    void use(Long purchaseId);

    void purchase(Long userId, Long tourId, int number);

    List<Purchase> findNotProcessed();
}
