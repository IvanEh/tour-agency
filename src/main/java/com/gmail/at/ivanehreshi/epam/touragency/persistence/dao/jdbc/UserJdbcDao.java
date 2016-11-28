package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ConnectionManager;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.JdbcTemplate;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
    private static final String COUNT_ORDERS_SQL = "SELECT COUNT(*) FROM `purchase` JOIN `user` ON `user`.id = `purchase`.user_id " 
    		+ "WHERE `user`.id=?";
    private static final String TOTAL_ORDERS_PRICE_SQL = "SELECT SUM(`price`) FROM `purchase` JOIN `user` ON `user`.id = `purchase`.user_id "
    		+ "WHERE `user`.id=?";

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
        User user = jdbcTemplate.queryObjects(UserJdbcDao::fromResultSet, READ_SQL, id).get(0);
        user.setRoles(readRoles(user.getId()));
        return user;
    }

    @Override
    public User read(String username) {
        User user = jdbcTemplate.queryObjects(UserJdbcDao::fromResultSet, READ_BY_USERNAME_SQL, username).get(0);
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
        return jdbcTemplate.queryObjects(UserJdbcDao::fromResultSet, FIND_ALL_SQL);
    }

    private static User fromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setPassword(rs.getString("password"));
        user.setDiscount(rs.getInt("discount"));
        return user;
    }

	@Override
	public int countPurchases(Long userId) {
		return jdbcTemplate.queryObjects((rs) -> rs.getInt(1), COUNT_ORDERS_SQL, userId).get(0);
	}

	@Override
	public BigDecimal computePurchasesTotalPrice(Long userId) {
		BigDecimal ans = jdbcTemplate.queryObjects((rs) -> rs.getBigDecimal(1), TOTAL_ORDERS_PRICE_SQL, userId).get(0);
		
		if(ans == null) {
			return new BigDecimal(0);
		}
		
		return ans;
	}
}
