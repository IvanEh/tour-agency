package com.gmail.at.ivanehreshi.epam.touragency.service.impl;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;

import java.util.*;

public class ReviewServiceImpl extends AbstractDaoService<Review, Long>
        implements ReviewService {
    private ReviewDao reviewDao;

    private UserDao userDao;

    public ReviewServiceImpl(ReviewDao reviewDao, UserDao userDao) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
    }

    @Override
    public void create(Review review) {
        checkConstraintsOrThrow(review);
        if(reviewDao.canVote(review.getAuthor().getId(), review.getTour().getId())) {
            review.setDate(new Date());
            Long id = reviewDao.create(review);
            review.setId(id);
        }
    }

    @Override
    public void update(Review review) {
        checkConstraintsOrThrow(review);
        review.setDate(new Date());
        super.update(review);
    }

    @Override
    public List<Review> findByTour(Long id) {
        List<Review> reviews = reviewDao.findByTour(id);
        for(Review review: reviews) {
            User user = userDao.read(review.getAuthor().getId());
            review.setAuthor(user);
        }
        return reviews;
    }

    @Override
    public boolean canVote(Long userId, Long tourId) {
        if(userId == null || tourId == null) {
            return false;
        }
        return reviewDao.canVote(userId, tourId);
    }

    @Override
    public Review findByPurchase(Long userId, Long tourId) {
        return reviewDao.findByPurchase(userId, tourId);
    }

    @Override
    public ReviewDao getBackingDao() {
        return reviewDao;
    }

    private void checkConstraintsOrThrow(Review review) {
        boolean ratingOk = review.getRating() > 0 && review.getRating() <= 5;
        if (!ratingOk) {
            throw new IllegalStateException("Rating should be > 0 and <= 5");
        }
    }
}
