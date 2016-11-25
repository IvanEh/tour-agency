package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.web.WebApplication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UpdateDiscountCommand implements Command {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, List<String> groups) {
        Long userId = Long.valueOf(req.getParameter("id"));
        Integer discount = Integer.valueOf(req.getParameter("discount"));
        System.out.println(userId + "=" + discount);
        User user = WebApplication.INSTANCE.getUserDao().read(userId);
        user.setDiscount(discount);
        WebApplication.INSTANCE.getUserDao().update(user);

        try {
            resp.sendRedirect("/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
