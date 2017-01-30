package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.controller.support.*;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;

public final class RegisterController extends Controller {

    private AuthService authService = ServiceLocator.INSTANCE.get(AuthService.class);

    @Override
    public void get(RequestService reqService) {
        User user = (User) reqService.getFlashParameter("user");
        reqService.putParameter("user", user);
        reqService.putParameter("error", reqService.getFlashParameter("error"));
    }

    @Override
    public void post(RequestService reqService) {
        User user = RequestExtractors.extractUser(reqService);

        Optional<String> invalid = Validation.check(user);
        if (invalid.isPresent()) {
            user.setPassword(null);
            reqService.putFlashParameter("user", user);
            reqService.putFlashParameter("error", invalid.get());

            reqService.redirect("/register.html");
            return;
        }

        if(!authService.register(user)) {
            reqService.putFlashParameter("error", "register.error.username");

            reqService.redirect("/register.html");
            return;
        }

        reqService.redirect("/login.html");
    }
}
