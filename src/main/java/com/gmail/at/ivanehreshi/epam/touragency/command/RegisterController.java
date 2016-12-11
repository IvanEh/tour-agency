package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.util.PasswordEncoder;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;

import java.util.Arrays;

public class RegisterController extends Controller {

    @Override
    public void post(RequestService reqService) {
        User user = new User();
        user.setUsername(reqService.getString("username"));
        user.setFirstName(reqService.getString("firstName"));
        user.setLastName(reqService.getString("lastName"));
        user.setPassword(PasswordEncoder.encodePassword(reqService.getString("password")));
        user.setRoles(Arrays.asList(Role.CUSTOMER));

        WebApplication.INSTANCE.getUserDao().create(user);
        reqService.redirect("/login.html");

    }
}
