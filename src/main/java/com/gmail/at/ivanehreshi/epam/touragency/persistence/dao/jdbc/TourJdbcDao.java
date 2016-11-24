package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ConnectionManager;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.JdbcTemplate;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TourJdbcDao implements TourDao {
    private static final String FIND_ALL_SQL = "SELECT * FROM tour";

    private ConnectionManager connectionManager;
    private JdbcTemplate jdbcTemplate;

    public TourJdbcDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    @Override
    public Long create(Tour tour) {
        return null;
    }

    @Override
    public Tour read(Long id) {
        return null;
    }

    @Override
    public void update(Tour tour) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Tour> findAll() {
        return jdbcTemplate.queryObjects(TourJdbcDao::fromResultSet, FIND_ALL_SQL);
    }

    private static Tour fromResultSet(ResultSet rs) throws SQLException {
        Tour tour = new Tour();
        tour.setId(rs.getLong("id"));
        tour.setTitle(rs.getString("title"));
        tour.setDescription(rs.getString("description"));
        tour.setPrice(rs.getBigDecimal("price"));
        tour.setType(TourType.values()[rs.getInt("type")]);
        tour.setHot(rs.getBoolean("hot"));
        return tour;
    }
}
