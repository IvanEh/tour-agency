package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, PK extends Serializable> {
    PK create(T t);
    T read(PK id);
    void update(T t);
    void delete(PK id);
    List<T> findAll();
}
