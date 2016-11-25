package com.gmail.at.ivanehreshi.epam.touragency.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface Command {
    void execute(HttpServletRequest req, HttpServletResponse resp, List<String> groups);
}
