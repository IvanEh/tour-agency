package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * An interface that represents a generic data access object
 * @param <T>  type of the target entity
 * @param <PK> primary key by which the target entity can be retrieved
 */
public interface Dao<T, PK extends Serializable> {
    PK create(T t);
    T read(PK id);
    void update(T t);
    void delete(PK id);
    List<T> findAll();
}
