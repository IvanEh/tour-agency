package com.gmail.at.ivanehreshi.epam.touragency.persistence.util;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setPassword(rs.getString("password"));
        user.setDiscount(rs.getInt("discount"));
        return user;
    }
}
