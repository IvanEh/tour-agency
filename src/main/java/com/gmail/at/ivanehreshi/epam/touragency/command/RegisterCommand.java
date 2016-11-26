package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.util.PasswordEncoder;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class RegisterCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, List<String> groups) {
        User user = new User();
        user.setUsername(req.getParameter("username"));
        user.setFirstName(req.getParameter("firstName"));
        user.setLastName(req.getParameter("lastName"));
        user.setPassword(PasswordEncoder.encodePassword(req.getParameter("password")));
        user.setRoles(Arrays.asList(Role.CUSTOMER));

        WebApplication.INSTANCE.getUserDao().create(user);
        try {
            resp.sendRedirect("/login.html");
        } catch (IOException e) {

        }
    }
}
