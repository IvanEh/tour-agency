package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.PurchaseDao;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;

import java.util.List;

public class PurchaseController extends Controller {
    @Override
    public void get(RequestService service) {
        super.get(service);

        User user = service.getUser();
        PurchaseDao purchaseDao = WebApplication.INSTANCE.getPurchaseDao();
        List<Purchase> purchases = purchaseDao.findByUser(user.getId());
        purchases.forEach(purchaseDao::deepen);
        service.putParameter("purchases", purchases);
    }
}
