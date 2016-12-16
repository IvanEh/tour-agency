package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

public class AgentUsersPageController extends Controller {
    private UserDao userDao = ServiceLocator.INSTANCE.get(UserDao.class);
    @Override
    public void get(RequestService reqService) {
        reqService.putParameter("users", userDao.findAllOrderByRegularity(true));
    }
}
