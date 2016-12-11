package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;

public class UpdateDiscountController extends Controller {
    @Override
    public void post(RequestService reqService) {
        Long userId = reqService.getLong("id");
        Integer discount = reqService.getInt("discount");
        User user = WebApplication.INSTANCE.getUserDao().read(userId);
        user.setDiscount(discount);
        WebApplication.INSTANCE.getUserDao().update(user);

        reqService.redirect("/agent/users.html");
    }
}
