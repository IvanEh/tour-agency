package com.gmail.at.ivanehreshi.epam.touragency.web;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.ConnectionManager;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.inmemory.PurchaseInMemoryDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.inmemory.TourInMemoryDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.TourJdbcDao;

import javax.servlet.ServletContext;

public enum WebApplication {
    INSTANCE;

    private ConnectionManager connectionManager;

    private TourDao tourDao;

    WebApplication() {
        connectionManager = new ConnectionManager();
        init();
    }

    private void init() {
        tourDao = new TourJdbcDao(connectionManager);
    }

    public TourDao getTourDao() {
        return tourDao;
    }

    public void setTourDao(TourDao tourDao) {
        this.tourDao = tourDao;
    }
}
