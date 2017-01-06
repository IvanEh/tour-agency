package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

public final class UpdateDiscountController extends Controller {
    private UserDao userDao = ServiceLocator.INSTANCE.get(UserDao.class);

    @Override
    public void post(RequestService reqService) {
        Long userId = reqService.getLong("id").orElse(null);
        Integer discount = reqService.getInt("discount").orElse(0);
        User user = userDao.read(userId);
        user.setDiscount(discount);
        userDao.update(user);

        reqService.redirect("/agent/users.html");
    }
}
