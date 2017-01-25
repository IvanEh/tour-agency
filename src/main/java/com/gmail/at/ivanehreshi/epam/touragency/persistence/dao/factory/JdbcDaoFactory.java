package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.factory;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.*;

public class JdbcDaoFactory implements DaoFactory {
    private ConnectionManager connectionManager;

    public JdbcDaoFactory(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public PurchaseDao getPurchaseDao() {
        return new PurchaseJdbcDao(connectionManager);
    }

    @Override
    public ReviewDao getReviewDao() {
        return new ReviewJdbcDao(connectionManager);
    }

    @Override
    public TourDao getTourDao() {
        return new TourJdbcDao(connectionManager);
    }

    @Override
    public TourImageDao getTourImageDao() {
        return new TourImageJdbcDao(connectionManager);
    }

    @Override
    public UserDao getUserDao() {
        return new UserJdbcDao(connectionManager);
    }
}
