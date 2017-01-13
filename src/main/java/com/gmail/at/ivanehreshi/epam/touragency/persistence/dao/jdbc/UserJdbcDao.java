package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.util.*;

import java.math.*;
import java.sql.*;
import java.util.*;

public class UserJdbcDao implements UserDao {
    private static final String CREATE_SQL = "INSERT INTO `user` (`username`, `firstName`, " +
            "`lastName`, `password`, `discount`) VALUES (?, ?, ?, ?, ?)";

    private static final String FIND_ALL_SQL = "SELECT * FROM `user`";

    private static final String READ_SQL = "SELECT * FROM `user` WHERE id=?";

    private static final String READ_BY_USERNAME_SQL = "SELECT * FROM `user` WHERE username=?";

    private static final String UPDATE_SQL = "UPDATE `user` SET `username`=?, `firstName`=?," +
            " `lastName`=?, `password`=?, `discount`=? WHERE `id`=?";

    private static final String DELETE_SQL = "DELETE FROM `user` WHERE id=?";

    private static final String GET_ROLES_SQL = "SELECT role_id FROM `user_role` WHERE `user_id`=?";

    private static final String ADD_ROLE_SQL = "INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (?, ?)";

    private static final String CLEAR_ROLES_SQL = "DELETE FROM `user_role` WHERE user_id=?";

    private static final String COUNT_ORDERS_SQL =
            "SELECT COUNT(*) FROM `purchase` JOIN `user` ON `user`.id = `purchase`.user_id "
            + "WHERE `user`.id=?";

    private static final String TOTAL_ORDERS_PRICE_SQL =
            "SELECT SUM(`price`) FROM `purchase` JOIN `user` ON `user`.id = `purchase`.user_id "
            + "WHERE `user`.id=?";

    private static final String FIND_ALL_BY_TOTAL_PRICE =
            "SELECT `user`.*, SUM(ifnull(price, 0)) AS total FROM `user`" +
            " LEFT JOIN `purchase` ON `user`.id = `purchase`.`user_id` GROUP BY `user`.`id` ORDER BY total DESC";

    private static final String FIND_ALL_BY_COUNT =
            "SELECT `user`.*, COUNT(price) AS total FROM `user` LEFT JOIN `purchase`" +
            " ON `user`.id = `purchase`.`user_id` GROUP BY `user`.`id` ORDER BY total DESC";

    private ConnectionManager connectionManager;

    private JdbcTemplate jdbcTemplate;

    public UserJdbcDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    @Override
    public Long create(User u) {
        Long id = jdbcTemplate.insert(CREATE_SQL, u.getUsername(), u.getFirstName(), u.getLastName(),
                u.getPassword(), u.getDiscount());
        addRoles(id, u.getRoles());
        return id;
    }

    @Override
    public User read(Long id) {
        User user = jdbcTemplate.queryObject(UserJdbcDao::fromResultSet, READ_SQL, id);
        if (user != null) {
            user.setRoles(readRoles(user.getId()));
        }

        return user;
    }

    @Override
    public User read(String username) {
        User user = jdbcTemplate.queryObject(UserJdbcDao::fromResultSet, READ_BY_USERNAME_SQL, username);
        if (user == null)
            return null;

        user.setRoles(readRoles(user.getId()));
        return user;
    }

    @Override
    public void update(User u) {
        jdbcTemplate.update(UPDATE_SQL, u.getUsername(), u.getFirstName(), u.getLastName(),
                u.getPassword(), u.getDiscount(), u.getId());
        updateRoles(u.getId(), u.getRoles());
    }

    @Override
    public void delete(Long id) {
        deleteRoles(id);
        jdbcTemplate.update(DELETE_SQL, id);
    }

    @Override
    public void addRoles(Long userId, List<Role> roles) {
        roles.forEach(role -> addRole(userId, role));
    }

    @Override
    public void addRole(Long userId, Role role) {
        jdbcTemplate.insert(ADD_ROLE_SQL, userId, role.ordinal() + 1);
    }

    @Override
    public void deleteRoles(Long userId) {
        jdbcTemplate.update(CLEAR_ROLES_SQL, userId);
    }

    @Override
    public void updateRoles(Long userId, List<Role> roles) {
        deleteRoles(userId);
        addRoles(userId, roles);
    }

    @Override
    public List<Role> readRoles(Long userId) {
        return jdbcTemplate.queryObjects((rs) -> Role.values()[rs.getInt("role_id") - 1], GET_ROLES_SQL, userId);
    }

    @Override
    public List<User> findAll() {
        List<User> users = jdbcTemplate.queryObjects(UserJdbcDao::fromResultSet,
                     FIND_ALL_SQL);
        users.forEach(u -> u.setRoles(readRoles(u.getId())));
        return users;
    }

    @Override
    public int countPurchases(Long userId) {
        return jdbcTemplate.queryObjects((rs) -> rs.getInt(1), COUNT_ORDERS_SQL, userId).get(0);
    }

    @Override
    public BigDecimal computePurchasesTotalPrice(Long userId) {
        BigDecimal ans = jdbcTemplate.queryObjects((rs) -> rs.getBigDecimal(1), TOTAL_ORDERS_PRICE_SQL, userId).get(0);

        if (ans == null) {
            return new BigDecimal(0);
        }

        return ans;
    }

    @Override
    public List<User> findAllOrderByRegularity(boolean byTotalPrice) {
        if (byTotalPrice) {
            return jdbcTemplate.queryObjects(UserJdbcDao::fromResultSet, FIND_ALL_BY_TOTAL_PRICE);
        }

        return jdbcTemplate.queryObjects(UserJdbcDao::fromResultSet, FIND_ALL_BY_COUNT);
    }

    private static User fromResultSet(ResultSet rs) throws SQLException {
        return UserMapper.map(rs);
    }

}
