package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.inmemory;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Purchase;
import com.gmail.at.ivanehreshi.epam.touragency.domain.User;

public class UserInMemoryDao extends AbstractInMemoryDao<User> {
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
