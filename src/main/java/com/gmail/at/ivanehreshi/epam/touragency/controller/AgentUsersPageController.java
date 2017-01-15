package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public final class AgentUsersPageController extends Controller {
    private UserService userService = ServiceLocator.INSTANCE.get(UserService.class);

    @Override
    public void get(RequestService reqService) {
        List<User> users = userService.findAllOrderByRegularity(true);
        reqService.putParameter("users", users);
    }
}
