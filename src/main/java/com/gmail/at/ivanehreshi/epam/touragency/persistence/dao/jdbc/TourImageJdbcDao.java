package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.util.*;

import java.util.*;

public class TourImageJdbcDao implements TourImageDao {
    private ConnectionManager connectionManager;

    private JdbcTemplate jdbcTemplate;

    public TourImageJdbcDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    @Override
    public Long create(TourImage tourImage) {
        Long id = jdbcTemplate.insert("INSERT INTO `tour_image`" +
                "(tour_id, image_url, thumbnail_url) VALUES (?, ?, ?)",
                tourImage.getTour().getId(),
                tourImage.getImageUrl().toString(),
                tourImage.getThumbnailUrl() == null ? null : tourImage.getThumbnailUrl().toString());
        return id;
    }

    @Override
    public TourImage read(Long id) {
        return jdbcTemplate.queryObject(TourImageMapper::map, "SELECT * " +
                "FROM `tour_image` WHERE id=?", id);
    }

    @Override
    public void update(TourImage tourImage) {
        jdbcTemplate.update("UPDATE `tour_image` SET tour_id=?, image_url=?, " +
                "thumbnail_url=? WHERE id=?", tourImage.getTour().getId(),
                tourImage.getImageUrl().toString(),
                tourImage.getThumbnailUrl().toString(),
                tourImage.getId());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM `tour_image` WHERE id=?", id);
    }

    @Override
    public List<TourImage> findAll() {
        return jdbcTemplate.queryObjects(TourImageMapper::map, "SELECT * " +
                "FROM `tour_image`");
    }
}
