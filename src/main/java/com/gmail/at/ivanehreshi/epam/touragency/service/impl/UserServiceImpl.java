package com.gmail.at.ivanehreshi.epam.touragency.service.impl;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;

import java.math.*;
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
        List<User> users = userDao.findAllOrderByRegularity(byTotalPrice);
        users.forEach(u -> u.setRoles(userDao.readRoles(u.getId())));
        return users;
    }

    @Override
    public int countPurchases(Long userId) {
        return userDao.countPurchases(userId);
    }

    @Override
    public BigDecimal computePurchasesTotalPrice(Long userId) {
        return userDao.computePurchasesTotalPrice(userId);
    }

    @Override
    public void makeTourAgent(Long userId) {
        List<Role> roles = userDao.readRoles(userId);
        if(!roles.contains(Role.TOUR_AGENT)) {
            roles.add(Role.TOUR_AGENT);
        }
        userDao.updateRoles(userId, roles);
        userDao.addRole(userId, Role.TOUR_AGENT);
    }
}
