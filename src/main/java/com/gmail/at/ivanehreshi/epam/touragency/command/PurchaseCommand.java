package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PurchaseCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, List<String> groups) {
        Long tourId = Long.valueOf(req.getParameter("tourId"));
        Long userId = Long.valueOf(req.getParameter("userId"));

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

        try {
            resp.sendRedirect("/index.html");
        } catch (IOException e) {}
    }
}
