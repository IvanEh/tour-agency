package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.factory;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;

public interface DaoFactory {
    PurchaseDao getPurchaseDao();
    ReviewDao getReviewDao();
    TourDao getTourDao();
    TourImageDao getTourImageDao();
    UserDao getUserDao();
}
