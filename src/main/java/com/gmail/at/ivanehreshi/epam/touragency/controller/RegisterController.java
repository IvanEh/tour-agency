package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

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

        user.setRoles(Collections.singletonList(Role.CUSTOMER));

        userDao.create(user);
        reqService.redirect("/login.html");

    }
}
