package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Role;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.Dao;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao extends Dao<User, Long> {
    void addRoles(Long userId, List<Role> roles);

    void addRole(Long userId, Role role);

    void deleteRoles(Long userId);

    void updateRoles(Long userId, List<Role> roles);

    List<Role> readRoles(Long userId);

    User read(String username);

    int countPurchases(Long userId);
    
    BigDecimal computePurchasesTotalPrice(Long userId);

    List<User> findAllOrderByRegularity(boolean byTotalPrice);
}
