package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;

import java.io.*;
import java.util.*;

public abstract class AbstractDaoService<T, PK extends Serializable> implements CrudService<T, PK> {
    @Override
    public void delete(PK id) {
        getDao().delete(id);
    }

    @Override
    public List<T> findAll() {
        return getDao().findAll();
    }

    @Override
    public T read(PK id) {
        return getDao().read(id);
    }

    @Override
    public void update(T t) {
        getDao().update(t);
    }

    public abstract Dao<T, PK> getDao();

}
