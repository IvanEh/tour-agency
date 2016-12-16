package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.util.PasswordEncoder;
import com.gmail.at.ivanehreshi.epam.touragency.web.ServiceLocator;

import java.util.Arrays;

public class RegisterController extends Controller {
    private UserDao userDao = ServiceLocator.INSTANCE.get(UserDao.class);

    @Override
    public void post(RequestService reqService) {
        User user = new User();
        user.setUsername(reqService.getString("username"));
        user.setFirstName(reqService.getString("firstName"));
        user.setLastName(reqService.getString("lastName"));
        user.setPassword(PasswordEncoder.encodePassword(reqService.getString("password")));
        user.setRoles(Arrays.asList(Role.CUSTOMER));

        userDao.create(user);
        reqService.redirect("/login.html");

    }
}
