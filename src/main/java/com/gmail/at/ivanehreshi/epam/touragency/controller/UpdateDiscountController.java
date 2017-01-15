package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

public final class UpdateDiscountController extends Controller {
    private UserService userService = ServiceLocator.INSTANCE.get(UserService.class);

    @Override
    public void post(RequestService reqService) {
        Long userId = reqService.getLong("id").orElse(null);
        Integer discount = reqService.getInt("discount").orElse(0);
        User user = userService.read(userId);
        user.setDiscount(discount);

        userService.update(user);

        reqService.redirect("/agent/users.html");
    }
}
