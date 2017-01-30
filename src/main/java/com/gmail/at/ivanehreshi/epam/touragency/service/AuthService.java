package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;

import javax.servlet.http.*;

public interface AuthService {
    boolean login(HttpServletRequest request, String user, String password);
    void logout(HttpServletRequest request);
    boolean register(User user);
}
