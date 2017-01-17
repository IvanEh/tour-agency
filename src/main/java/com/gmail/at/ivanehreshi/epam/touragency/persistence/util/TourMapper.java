package com.gmail.at.ivanehreshi.epam.touragency.persistence.util;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;

import java.math.*;
import java.sql.*;
import java.util.*;

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
       tour.setAvgRating(Optional.ofNullable(rs.getBigDecimal("avg_rating"))
               .map(BigDecimal::doubleValue)
               .orElse(null));
       tour.setVotesCount(rs.getInt("votes_count"));

       return tour;
   }
}
