package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.PurchaseDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public final class PurchaseController extends Controller {
    private PurchaseDao purchaseDao = ServiceLocator.INSTANCE.get(PurchaseDao.class);
    private TourDao tourDao = ServiceLocator.INSTANCE.get(TourDao.class);
    private UserDao userDao = ServiceLocator.INSTANCE.get(UserDao.class);

    private static final double HUNDR_PERCENT = 100.0;

    @Override
    public void get(RequestService reqService) {
        User user = reqService.getUser();
        List<Purchase> purchases = purchaseDao.findByUser(user.getId());
        purchases.forEach(purchaseDao::deepen);
        reqService.putParameter("purchases", purchases);
    }

    @Override
    public void post(RequestService reqService) {
        Long tourId = reqService.getLong("tourId");
        Long userId = reqService.getUser().getId();

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

        purchaseDao.create(purchase);

        reqService.redirect("/user/purchases.html");
    }
}
