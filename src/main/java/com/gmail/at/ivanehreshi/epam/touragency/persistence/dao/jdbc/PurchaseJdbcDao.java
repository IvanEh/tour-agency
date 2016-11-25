package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ConnectionManager;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.JdbcTemplate;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.PurchaseDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PurchaseJdbcDao implements PurchaseDao {
    private static final String CREATE_SQL = "INSERT INTO `purchase` (`user_id`, `tour_id`, `date`, `price`) " +
            "VALUES (?, ?, ?, ?)";
    private static final String FIND_ALL_SQL = "SELECT * FROM `purchase`";
    private static final String FIND_BY_USER_SQL = "SELECT * FROM `purchase` WHERE user_id=?";
    private static final String READ_SQL = "SELECT * FROM `purchase` WHERE id=?";
    private static final String UPDATE_SQL = "UPDATE `purchase` SET `user_id`=?, `tour_id`=?," +
            " `date`=?, `price`=? WHERE `id`=?";
    private static final String DELETE_SQL = "DELETE FROM `purchase` WHERE id=?";

    private ConnectionManager connectionManager;
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;
    private TourDao tourDao;

    public PurchaseJdbcDao(ConnectionManager connectionManager, UserDao userDao, TourDao tourDao) {
        this.connectionManager = connectionManager;
        this.jdbcTemplate = new JdbcTemplate(connectionManager);
        this.userDao = userDao;
        this.tourDao = tourDao;
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
    public Purchase deepen(Purchase purchase) {
        purchase.setUser(userDao.read(purchase.getUser().getId()));
        purchase.setTour(tourDao.read(purchase.getTour().getId()));
        return purchase;
    }

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
        Purchase purchase = new Purchase();
        purchase.setId(rs.getLong("id"));
        purchase.setUser(new User(rs.getLong("user_id")));
        purchase.setTour(new Tour(rs.getLong("tour_id")));
        purchase.setPrice(rs.getBigDecimal("price"));
        purchase.setDate(rs.getDate("date"));
        return purchase;
    }
}
