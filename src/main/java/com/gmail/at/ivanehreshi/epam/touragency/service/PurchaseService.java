package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;

import java.util.*;

public interface PurchaseService extends CrudService<Purchase, Long> {
    List<Purchase> findByUser(Long id);

    Purchase purchase(Long userId, Long tourId);
}
