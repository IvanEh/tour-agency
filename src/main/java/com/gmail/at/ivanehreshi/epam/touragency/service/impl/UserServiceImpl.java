package com.gmail.at.ivanehreshi.epam.touragency.service.impl;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;

import java.util.*;

public class UserServiceImpl extends AbstractDaoService<User, Long>
        implements UserService {

    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void create(User user) {
        Long id = userDao.create(user);
        user.setId(id);
    }

    @Override
    public UserDao getDao() {
        return userDao;
    }

    @Override
    public List<User> findAllOrderByRegularity(boolean byTotalPrice) {
        return userDao.findAllOrderByRegularity(byTotalPrice);
    }
}
