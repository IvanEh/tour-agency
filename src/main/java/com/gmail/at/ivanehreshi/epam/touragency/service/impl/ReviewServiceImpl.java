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
        if(reviewDao.canVote(review.getAuthor().getId(), review.getTour().getId())) {
            review.setDate(new Date());
            Long id = reviewDao.create(review);
            review.setId(id);
        }
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
    public ReviewDao getDao() {
        return reviewDao;
    }

}
