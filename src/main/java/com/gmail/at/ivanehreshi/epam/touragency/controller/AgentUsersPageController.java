package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;
import java.util.stream.*;

public final class AgentUsersPageController extends Controller {
    private UserService userService = ServiceLocator.INSTANCE.get(UserService.class);

    @Override
    public void get(RequestService reqService) {
        List<User> users = userService.findAllOrderByRegularity(true);
        List<UserPurchaseSummaryDto> summaries = users.stream()
                     .map(u -> new UserPurchaseSummaryDto(u, userService.countPurchases(u.getId()),
                                userService.computePurchasesTotalPrice(u.getId()).intValue()))
                     .collect(Collectors.toList());
        reqService.putParameter("summaries", summaries);
    }

    public static class UserPurchaseSummaryDto {
        private User user;
        private int purchaseCount;
        private int totalPrice;

        public UserPurchaseSummaryDto(User user, int purchaseCount, int totalPrice) {
            this.user = user;
            this.purchaseCount = purchaseCount;
            this.totalPrice = totalPrice;
        }

        public int getPurchaseCount() {
            return purchaseCount;
        }

        public int getTotalPrice() {
            return totalPrice;
        }

        public User getUser() {
            return user;
        }
    }
}
