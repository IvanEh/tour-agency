package com.gmail.at.ivanehreshi.epam.touragency.persistence.util;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TourMapper {
   public static Tour map(ResultSet rs) throws SQLException {
       Tour tour = new Tour();
       tour.setId(rs.getLong("id"));
       tour.setTitle(rs.getString("title"));
       tour.setDescription(rs.getString("description"));
       tour.setPrice(rs.getBigDecimal("price"));
       tour.setType(TourType.values()[rs.getInt("type")]);
       tour.setHot(rs.getBoolean("hot"));
       tour.setEnabled(rs.getBoolean("enabled"));
       return tour;
   }
}
