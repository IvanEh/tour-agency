package com.gmail.at.ivanehreshi.epam.touragency.persistence.util;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;

import java.sql.*;

public class ReviewMapper {
    public static Review map(ResultSet rs) throws SQLException {
        Review review = new Review();

        review.setId(rs.getLong("id"));
        review.setText(rs.getString("text"));
        review.setRating(rs.getInt("rating"));
        review.setAuthor(new User(rs.getLong("author_id")));
        review.setTour(new Tour(rs.getLong("tour_id")));
        review.setDate(rs.getDate("date"));

        return review;
    }
}
