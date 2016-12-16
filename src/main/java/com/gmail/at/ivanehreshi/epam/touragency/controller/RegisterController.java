package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.PasswordEncoder;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

import java.util.Arrays;

public final class RegisterController extends Controller {
    private UserDao userDao = ServiceLocator.INSTANCE.get(UserDao.class);

    @Override
    public void post(RequestService reqService) {
        User user = new User();

        user.setUsername(reqService.getString("username"));
        user.setFirstName(reqService.getString("firstName"));
        user.setLastName(reqService.getString("lastName"));

        String pass = reqService.getString("password");
        String encodedPass = PasswordEncoder.encodePassword(pass);
        user.setPassword(encodedPass);

        user.setRoles(Arrays.asList(Role.CUSTOMER));

        userDao.create(user);
        reqService.redirect("/login.html");

    }
}
