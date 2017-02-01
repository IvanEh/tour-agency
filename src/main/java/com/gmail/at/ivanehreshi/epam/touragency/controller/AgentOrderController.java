package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public class AgentOrderController extends Controller {
    private PurchaseService purchaseService =
            ServiceLocator.INSTANCE.get(PurchaseService.class);

    @Override
    public void get(RequestService reqService) {
        Long id = reqService.getLong("id").orElse(null);

        if(id != null) {
            Purchase purchase = purchaseService.read(id);
            reqService.setPageAttribute("purchase", purchase);
        }

        List<Purchase> purchases = purchaseService.findNotProcessed();
        reqService.setPageAttribute("purchases", purchases);
    }

    @Override
    public void post(RequestService reqService) {
        String action = reqService.getString("action");
        Long purchaseId = reqService.getLong("id").get();
        switch (action) {
            case "acknowledge":
                purchaseService.acknowledge(purchaseId);
                break;
            case "cancel":
                purchaseService.cancel(purchaseId);
                break;
            case "used":
                purchaseService.use(purchaseId);
                break;
        }

        reqService.redirect("/agent/order.html");
    }
}
