package com.gmail.at.ivanehreshi.epam.touragency.persistence.util;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchaseMapper {
    public static Purchase map(ResultSet rs) throws SQLException {
        Purchase purchase = new Purchase();
        purchase.setId(rs.getLong("id"));
        purchase.setUser(new User(rs.getLong("user_id")));
        purchase.setTour(new Tour(rs.getLong("tour_id")));
        purchase.setPrice(rs.getBigDecimal("price"));
        purchase.setDate(rs.getDate("date"));
        return purchase;
    }
}
