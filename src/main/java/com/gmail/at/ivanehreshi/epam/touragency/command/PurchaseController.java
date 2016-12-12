package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.PurchaseDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.web.ObjectFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PurchaseController extends Controller {
    private PurchaseDao purchaseDao = ObjectFactory.INSTANCE.get(PurchaseDao.class);
    private TourDao tourDao = ObjectFactory.INSTANCE.get(TourDao.class);
    private UserDao userDao = ObjectFactory.INSTANCE.get(UserDao.class);

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

        BigDecimal discount = new BigDecimal((100 - user.getDiscount()) / 100.0);
        BigDecimal price = tour.getPrice().multiply(discount);

        purchase.setTour(new Tour(tourId));
        purchase.setUser(new User(userId));
        purchase.setDate(new Date());
        purchase.setPrice(price);

        purchaseDao.create(purchase);

        reqService.redirect("/user/purchases.html");
    }
}
