package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.transaction.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.util.*;

import java.util.*;

public class ReviewJdbcDao implements ReviewDao {

    private ConnectionManager connectionManager;

    private JdbcTemplate jdbcTemplate;

    public ReviewJdbcDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.jdbcTemplate = new JdbcTemplate(connectionManager);
    }

    @Override
    public Long create(Review review) {

        Long id[] = {null};
        Transaction.tx(connectionManager, () -> {
            id[0] = jdbcTemplate.insert("INSERT INTO `review`(text, rating, date, " +
                            "author_id, tour_id) VALUES (?, ?, ?, ?, ?)", review.getText(),
                    review.getRating(), review.getDate(), review.getAuthor().getId(),
                    review.getTour().getId());

            jdbcTemplate.update("UPDATE `tour` SET avg_rating=" +
                            "(IFNULL(avg_rating, 0)*votes_count + ?)/(votes_count+1), " +
                            "votes_count=votes_count+1 WHERE id=?",
                    review.getRating(), review.getTour().getId());
        });

        return id[0];
    }

    @Override
    public Review read(Long id) {
        return jdbcTemplate.queryObject("SELECT * FROM `review` WHERE id=?", ReviewMapper::map,
                id);
    }

    @Override
    public void update(Review review) {
        Transaction.tx(connectionManager, () -> {
            int delta = review.getRating() - jdbcTemplate.queryObject("SELECT rating " +
                    "FROM `review` WHERE id=?", rs -> rs.getInt(1), review.getId());

            jdbcTemplate.update("UPDATE `review` SET text=?, rating=?, date=?, " +
                            "author_id=?, tour_id=? WHERE id=?", review.getText(), review.getRating(),
                    review.getDate(), review.getAuthor().getId(), review.getTour().getId(),
                    review.getId());

            jdbcTemplate.update("UPDATE `tour` SET avg_rating=" +
                    "(avg_rating*votes_count+?)/votes_count", delta);

        });
    }

    @Override
    public void delete(Long id) {
        Transaction.tx(connectionManager, () -> {
            Long tourIdWrapper[] = new Long[1];
            Integer ratingWrapper[] = new Integer[1];

            jdbcTemplate.query("SELECT * FROM `review` WHERE id=?", rs -> {
                rs.next();
                ratingWrapper[0] = rs.getInt("rating");
                tourIdWrapper[0] = rs.getLong("tour_id");
            }, id);

            jdbcTemplate.update("DELETE FROM `review` WHERE id=?", id);


            jdbcTemplate.update("UPDATE `tour` SET avg_rating = CASE votes_count " +
                    "WHEN 1 THEN NULL " +
                    "ELSE (avg_rating*votes_count-?)/(votes_count-1) END, " +
                    "votes_count=votes_count-1 WHERE id=?", ratingWrapper[0], tourIdWrapper[0]);
        });
    }

    @Override
    public List<Review> findAll() {
        return jdbcTemplate.queryObjects("SELECT * FROM `review` ORDER BY id DESC",
                ReviewMapper::map);
    }

    @Override
    public List<Review> findByTour(Long id) {
        return jdbcTemplate.queryObjects("SELECT * FROM `review` WHERE tour_id=? " +
                "ORDER BY id DESC", ReviewMapper::map,
                id);
    }

    @Override
    public boolean canVote(Long userId, Long tourId) {
        boolean flagWrapper[] = new boolean[]{false};
        jdbcTemplate.query("SELECT id FROM `review` WHERE author_id=? AND tour_id=?",
                rs -> flagWrapper[0] = !rs.next(), userId, tourId);

        jdbcTemplate.query("SELECT id FROM `purchase` WHERE user_id=? AND tour_id=?",
                rs -> flagWrapper[0] &= rs.next(), userId, tourId);

        return flagWrapper[0];
    }

    @Override
    public Review findByPurchase(Long userId, Long tourId) {
        return jdbcTemplate.queryObject("SELECT * FROM `review` WHERE author_id=? " +
                "AND tour_id=?", ReviewMapper::map, userId, tourId);
    }
}
