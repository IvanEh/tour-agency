package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.web.ServiceLocator;

public class UpdateDiscountController extends Controller {
    private UserDao userDao = ServiceLocator.INSTANCE.get(UserDao.class);

    @Override
    public void post(RequestService reqService) {
        Long userId = reqService.getLong("id");
        Integer discount = reqService.getInt("discount");
        User user = userDao.read(userId);
        user.setDiscount(discount);
        userDao.update(user);

        reqService.redirect("/agent/users.html");
    }
}
