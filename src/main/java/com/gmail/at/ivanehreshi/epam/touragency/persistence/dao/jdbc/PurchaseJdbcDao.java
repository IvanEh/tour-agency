package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.util.*;

import java.sql.*;
import java.util.*;

public class PurchaseJdbcDao implements PurchaseDao {
    private static final String CREATE_SQL =
            "INSERT INTO `purchase` (`user_id`, `tour_id`, `date`, `price`) " +
            "VALUES (?, ?, ?, ?)";

    private static final String FIND_ALL_SQL =
            "SELECT * FROM `purchase` ORDER BY id DESC";

    private static final String FIND_BY_USER_SQL =
            "SELECT * FROM `purchase` WHERE user_id=?";

    private static final String READ_SQL = "SELECT * FROM `purchase` WHERE id=?";

    private static final String UPDATE_SQL =
            "UPDATE `purchase` SET `user_id`=?, `tour_id`=?," +
            " `date`=?, `price`=? WHERE `id`=?";

    private static final String READ_TOUR_SQL = "SELECT * FROM tour WHERE id=?";

    private static final String READ_USER_SQL = "SELECT * FROM `user` WHERE id=?";

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
        Purchase purchase = jdbcTemplate.queryObject(PurchaseJdbcDao::fromResultSet,
                READ_SQL, id);
        return purchase;
    }

    @Override
    public Purchase deepen(Purchase purchase) {
        JdbcTemplate jdbcTemplate1 = new JdbcTemplate(connectionManager);

//        jdbcTemplate1.startTransaction();

        purchase.setUser(jdbcTemplate1.queryObject(UserMapper::map, READ_USER_SQL,
                purchase.getUser().getId()));
        purchase.setTour(jdbcTemplate1.queryObject(TourMapper::map, READ_TOUR_SQL,
                purchase.getTour().getId()));

//        jdbcTemplate1.commit();
        return purchase;
    }

    @Override
    public List<Purchase> findByUser(Long userId) {
        return jdbcTemplate.queryObjects(PurchaseJdbcDao::fromResultSet, FIND_BY_USER_SQL, userId);
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
        return PurchaseMapper.map(rs);
    }
}
