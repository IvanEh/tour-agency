package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public final class PurchasesController extends Controller {
    private static final int MAX_DESC_LENGTH = 300;

    private PurchaseService purchaseService =
            ServiceLocator.INSTANCE.get(PurchaseService.class);


    @Override
    public void get(RequestService reqService) {
        User user = reqService.loadUser().orElse(null);

        List<Group<Tour, Purchase>> purchases =
                purchaseService.findByUserGroupByTourOrdered(user.getId());
        purchases.stream().map(Group::getKey)
                 .forEach(t -> t.setDescription(TextShrinker.shrink(t.getDescription(),
                         MAX_DESC_LENGTH)));

        reqService.setPageAttribute("purchaseGroups", purchases);
    }

    @Override
    public void post(RequestService reqService) {
        Long tourId = reqService.getLong("tourId").orElse(null);
        Long userId = reqService.getUser().orElse(null).getId();
        int number = reqService.getInt("units").get();

        purchaseService.purchase(userId, tourId, number);

        reqService.redirect("/user/purchase.html?tourId=" + tourId);
    }
}
