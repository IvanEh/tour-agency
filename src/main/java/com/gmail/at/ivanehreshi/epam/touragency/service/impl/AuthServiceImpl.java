package com.gmail.at.ivanehreshi.epam.touragency.service.impl;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.apache.logging.log4j.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LogManager.getLogger(AuthServiceImpl.class);

    private UserService userService;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean login(HttpServletRequest request, String username, String password) {
        try {
            request.login(username, password);
            return true;
        } catch (ServletException e) {
            return false;
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
            LOGGER.error("Error occurred when user tried to logout", e);
        }
    }

    @Override
    public void register(User user, String plainPassw) {
        user.setPassword(PasswordEncoder.encodePassword(plainPassw));
        user.setRoles(Collections.singletonList(Role.CUSTOMER));
        userService.create(user);
    }
}
