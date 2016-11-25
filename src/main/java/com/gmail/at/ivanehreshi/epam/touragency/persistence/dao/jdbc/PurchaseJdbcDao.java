package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ConnectionManager;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.JdbcTemplate;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.PurchaseDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PurchaseJdbcDao implements PurchaseDao {
    private static final String CREATE_SQL = "INSERT INTO `purchase` (`user_id`, `tour_id`, `date`, `price`) " +
            "VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_SQL = "SELECT * FROM `purchase`";
    private static final String READ_SQL = "SELECT * FROM `purchase` WHERE id=?";
    private static final String UPDATE_SQL = "UPDATE `purchase` SET `user_id`=?, `tour_id`=?," +
            " `date`=?, `price`=? WHERE `id`=?";
    private static final String DELETE_SQL = "DELETE FROM `purchase` WHERE id=?";

    private ConnectionManager connectionManager;
    private JdbcTemplate jdbcTemplate;

    public PurchaseJdbcDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    @Override
    public Long create(Purchase p) {
        Long id = jdbcTemplate.insert(CREATE_SQL, p.getUser().getId(), p.getTour().getId(), p.getDate(),
                p.getPrice());
        return id;
    }

    @Override
    public Purchase read(Long id) {
        Purchase purchase = jdbcTemplate.queryObjects(PurchaseJdbcDao::fromResultSet, READ_SQL, id).get(0);
        return purchase;
    }

    @Override
    public void update(Purchase p) {
        jdbcTemplate.update(UPDATE_SQL, p.getUser().getId(), p.getTour().getId(), p.getDate(),
                p.getPrice(), p.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public List<Purchase> findAll() {
        return jdbcTemplate.queryObjects(PurchaseJdbcDao::fromResultSet, FIND_ALL_SQL);
    }


    private static Purchase fromResultSet(ResultSet rs) throws SQLException {
        Purchase purchase = new Purchase();
        purchase.setId(rs.getLong("id"));
        purchase.setUser(new User(rs.getLong("user_id")));
        purchase.setTour(new Tour(rs.getLong("tour_id")));
        purchase.setPrice(rs.getBigDecimal("price"));
        purchase.setDate(rs.getDate("date"));
        return purchase;
    }
}
