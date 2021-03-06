package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;

import java.math.*;
import java.util.*;

public interface UserService extends CrudService<User, Long> {
    List<User> findAllOrderByRegularity(boolean byTotalPrice);

    int countPurchases(Long userId);

    BigDecimal computePurchasesTotalPrice(Long userId);

    void makeTourAgent(Long userId);

    int computeDiscount(Long userId, Long tourId);
}
