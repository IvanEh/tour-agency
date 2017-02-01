package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.impl.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.junit.*;

import java.math.*;
import java.util.*;

import static org.junit.Assert.*;

public class TourServiceTest {
    private ConnectionManager cm;
    private ReviewService reviewService;
    private TourServiceImpl tourService;
    private UserServiceImpl userService;
    private PurchaseJdbcDao purchaseDao;

    private Tour tour1;
    private Tour tour2;
    private User user1;
    private User user2;

    @Before
    public void setUp() throws Exception {
        cm = H2Db.init("database.sql");
        reviewService = new ReviewServiceImpl(new ReviewJdbcDao(cm), new UserJdbcDao(cm));
        tourService = new TourServiceImpl(new TourJdbcDao(cm));
        userService = new UserServiceImpl(new UserJdbcDao(cm), new TourJdbcDao(cm));
        purchaseDao = new PurchaseJdbcDao(cm);
        prepareData();
    }

    @After
    public void tearDown() throws Exception {
        new JdbcTemplate(cm).executeSqlFile(
                ResourcesUtil.getResourceFile("clear-database.sql"));
    }

    private void prepareData() {
        tour1 = TestData.getRecreationTour();
        tour2 = TestData.getShoppingTour();
        user1 = TestData.getUser();
        user1.setRoles(Arrays.asList(Role.TOUR_AGENT));
        user2 = TestData.getUser();
        user2.setUsername("u");
        user2.setRoles(Arrays.asList(Role.TOUR_AGENT));
        Purchase purchase1 = new Purchase(user1, tour1, new BigDecimal("0"));
        Purchase purchase2 = new Purchase(user2, tour1, new BigDecimal("0"));

        tourService.create(tour1);
        tourService.create(tour2);
        userService.create(user1);
        userService.create(user2);
        purchaseDao.create(purchase1);
        purchaseDao.create(purchase2);
    }

    private Review getReview(Tour tour) {
        Review review = new Review();
        review.setAuthor(user1);
        review.setTour(tour);
        review.setRating(2);
        review.setText("");
        return review;
    }

    @Test
    public void testCreateReadById() {
        Review review = getReview(tour1);

        reviewService.create(review);
        Review dbReview = reviewService.read(review.getId());
        Tour dbTour = tourService.read(tour1.getId());

        fuzzyEquals(review, dbReview);
        assertEquals(2.0, dbTour.getAvgRating(), 0);
        assertEquals(1, dbTour.getVotesCount());
    }

    @Test
    public void testCreateTwoReadById() {
        Review review = getReview(tour1);

        reviewService.create(review);
        Review dbReview = reviewService.read(review.getId());
        Tour dbTour = tourService.read(tour1.getId());

        fuzzyEquals(review, dbReview);
        assertEquals(2.0, dbTour.getAvgRating(), 0);
        assertEquals(1, dbTour.getVotesCount());

        review = getReview(tour1);
        review.setAuthor(user2);
        review.setRating(3);

        reviewService.create(review);
        dbTour = tourService.read(tour1.getId());

        assertEquals(2.5, dbTour.getAvgRating(), 0);
        assertEquals(2, dbTour.getVotesCount());
    }

    @Test
    public void testUpdate() {
        Review review = getReview(tour1);

        reviewService.create(review);

        review.setRating(4);
        reviewService.update(review);

        Review dbReview = reviewService.read(review.getId());
        Tour dbTour = tourService.read(tour1.getId());

        assertEquals(4.0, dbTour.getAvgRating(), 0);
        assertEquals(1, dbTour.getVotesCount());
    }

    @Test
    public void testFindAll() {
        Review review1 = getReview(tour1);
        Review review2 = getReview(tour1);
        review2.setAuthor(user2);

        reviewService.create(review1);
        reviewService.create(review2);

        List<Review> reviews = reviewService.findAll();
        fuzzyEquals(review1, reviews.get(1));
        fuzzyEquals(review2, reviews.get(0));
    }

    @Test
    public void testDelete() {
        Review review = getReview(tour1);

        reviewService.create(review);
        reviewService.delete(review.getId());

        Review dbReview = reviewService.read(review.getId());
        assertNull(dbReview);
    }

    private void fuzzyEquals(Review expected, Review actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getRating(), actual.getRating());
        assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        assertEquals(expected.getTour().getId(), actual.getTour().getId());
    }
}
