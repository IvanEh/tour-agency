package com.gmail.at.ivanehreshi.epam.touragency.service.impl;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;

import java.math.*;
import java.util.*;

public class PurchaseServiceImpl extends AbstractDaoService<Purchase, Long>
        implements PurchaseService {

    private PurchaseDao purchaseDao;
    private TourDao tourDao;
    private UserDao userDao;

    private static final double HUNDR_PERCENT = 100.0;

    public PurchaseServiceImpl(PurchaseDao purchaseDao, TourDao tourDao, UserDao userDao) {
        this.purchaseDao = purchaseDao;
        this.tourDao = tourDao;
        this.userDao = userDao;
    }

    @Override
    public void create(Purchase purchase) {
        Long id = purchaseDao.create(purchase);
        purchase.setId(id);
    }

    @Override
    public List<Purchase> findByUser(Long id) {
        List<Purchase> purchases = purchaseDao.findByUser(id);
        purchases.forEach(purchaseDao::deepen);
        return purchases;
    }

    @Override
    public Purchase purchase(Long userId, Long tourId) {
        Purchase purchase = new Purchase();
        Tour tour = tourDao.read(tourId);
        User user = userDao.read(userId);

        double discountDouble = (HUNDR_PERCENT - user.getDiscount()) / HUNDR_PERCENT;
        BigDecimal discount = new BigDecimal(discountDouble);
        BigDecimal price = tour.getPrice().multiply(discount);

        purchase.setTour(new Tour(tourId));
        purchase.setUser(new User(userId));
        purchase.setDate(new Date());
        purchase.setPrice(price);

        Long id = purchaseDao.create(purchase);
        purchase.setId(id);

        return purchase;
    }

    @Override
    public PurchaseDao getDao() {
        return purchaseDao;
    }

}
