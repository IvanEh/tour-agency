package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.inmemory;

import com.gmail.at.ivanehreshi.epam.touragency.domain.User;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.UserDao;

public class UserInMemoryDao extends AbstractInMemoryDao<User> implements UserDao{
    public UserInMemoryDao() {
        super(User.class);
    }

    @Override
    public Long create(User user) {
        Long key = super.create(user);
        user.setId(key);
        return key;
    }

    @Override
    public void update(User user) {
        getEntities().replace(user.getId(), user);
    }
}
