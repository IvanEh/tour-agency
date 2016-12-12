package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.PurchaseDao;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PurchaseController extends Controller {
    @Override
    public void get(RequestService reqService) {
        User user = reqService.getUser();
        PurchaseDao purchaseDao = WebApplication.INSTANCE.getPurchaseDao();
        List<Purchase> purchases = purchaseDao.findByUser(user.getId());
        purchases.forEach(purchaseDao::deepen);
        reqService.putParameter("purchases", purchases);
    }

    @Override
    public void post(RequestService reqService) {
        Long tourId = reqService.getLong("tourId");
        Long userId = reqService.getUser().getId();

        Purchase purchase = new Purchase();
        Tour tour = WebApplication.INSTANCE.getTourDao().read(tourId);
        User user = WebApplication.INSTANCE.getUserDao().read(userId);

        BigDecimal discount = new BigDecimal((100 - user.getDiscount()) / 100.0);
        BigDecimal price = tour.getPrice().multiply(discount);

        purchase.setTour(new Tour(tourId));
        purchase.setUser(new User(userId));
        purchase.setDate(new Date());
        purchase.setPrice(price);

        WebApplication.INSTANCE.getPurchaseDao().create(purchase);

        reqService.redirect("/user/purchases.html");
    }
}
