package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.inmemory;

import com.gmail.at.ivanehreshi.epam.touragency.persistence.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class AbstractInMemoryDao<T> implements Dao<T, Long> {
    private Class<T> clazz;

    private Map<Long, T> entities = new HashMap<>();

    private long nextId = 0;

    protected AbstractInMemoryDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Long create(T t) {
        long nextId = generateNextId();
        entities.put(nextId, t);
        return nextId;
    }

    @Override
    public void delete(Long id) {
        entities.remove(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<T>(entities.values());
    }

    @Override
    public T read(Long id) {
        return entities.get(id);
    }

    public Class<T> getClazz() {
        return clazz;
    }

    protected Map<Long, T> getEntities() {
        return entities;
    }

    protected long generateNextId() {
        ++nextId;
        return nextId;
    }
}
