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
    private static final String CREATE_SQL = "INSERT INTO `tour` (`title`, `description`, `type`, `hot`, `price`) VALUES (?, ?, ?, ?, ?)";
    private static final String FIND_ALL_SQL = "SELECT * FROM tour";
    private static final String READ_SQL = "SELECT * FROM tour WHERE id=?";
    private static final String UPDATE_SQL = "UPDATE `tour` SET `title`=?, `description`=?, `type`=?, `hot`=?, `price`=? WHERE `id`=?";
    private static final String DELETE_SQL = "DELETE FROM tour WHERE id=?";

    private ConnectionManager connectionManager;
    private JdbcTemplate jdbcTemplate;

    public TourJdbcDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    @Override
    public Long create(Tour t) {
        return jdbcTemplate.insert(CREATE_SQL, t.getTitle(), t.getDescription(), t.getType().ordinal(),
                t.isHot(), t.getPrice());
    }

    @Override
    public Tour read(Long id) {
        return jdbcTemplate.queryObjects(TourJdbcDao::fromResultSet, READ_SQL, id).get(0);
    }

    @Override
    public void update(Tour t) {
        jdbcTemplate.update(UPDATE_SQL, t.getTitle(), t.getDescription(),
                t.getType().ordinal(), t.isHot(), t.getPrice(), t.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
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
