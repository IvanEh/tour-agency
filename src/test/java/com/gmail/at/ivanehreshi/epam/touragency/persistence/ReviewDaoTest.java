package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ReviewDaoTest {
    private ConnectionManager connectionManager;

    private ReviewDao reviewDao;

    private TourDao tourDao;

    private UserDao userDao;

    private TestData.ReviewTestData data;

    @Before
    public void setUp() throws Exception {
        connectionManager = H2Db.init("database.sql");
        reviewDao = new ReviewJdbcDao(connectionManager);
        tourDao = new TourJdbcDao(connectionManager);
        userDao = new UserJdbcDao(connectionManager);
        data = TestData.getReviewTestData(userDao, tourDao);
    }

    @After
    public void tearDown() throws Exception {
        new JdbcTemplate(connectionManager).executeSqlFile(
                ResourcesUtil.getResourceFile("clear-database.sql"));
    }

    @Test
    public void testCreateRead() {
        Review review = TestData.getReview(data.user1, data.tour);
        Long id = reviewDao.create(review);

        assertNotNull(id);

        review.setId(id);

        Review dbReview = reviewDao.read(id);
        assertWeakEquals(review, dbReview);

        Tour tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(4.0), tour.getAvgRating());
        assertEquals(1, tour.getVotesCount());
    }

    @Test
    public void testConsistency() {
        Review review1 = TestData.getReview(data.user1, data.tour);
        Review review2 = TestData.getReview(data.user1, data.tour);
        review2.setRating(3);

        Long id = reviewDao.create(review1);
        assertNotNull(id);
        review1.setId(id);

        id = reviewDao.create(review2);
        assertNotNull(id);
        review2.setId(id);

        Tour tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(3.5), tour.getAvgRating());
        assertEquals(2, tour.getVotesCount());
    }

    @Test
    public void testUpdate() {
        Review review = TestData.getReview(data.user1, data.tour);
        Long id = reviewDao.create(review);
        assertNotNull(id);

        review.setId(id);
        review.setRating(2);

        Tour tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(4.0), tour.getAvgRating());
        assertEquals(1, tour.getVotesCount());

        reviewDao.update(review);

        Review dbReview = reviewDao.read(id);
        assertWeakEquals(review, dbReview);

        tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(2.0), tour.getAvgRating());
        assertEquals(1, tour.getVotesCount());
    }

    @Test
    public void testUpdateGivenTwoRecords() {
        Review review1 = TestData.getReview(data.user1, data.tour);
        Review review2 = TestData.getReview(data.user2, data.tour);
        review2.setRating(5);

        Long id1 = reviewDao.create(review1);
        Long id2 = reviewDao.create(review2);
        assertNotNull(id1);
        assertNotNull(id2);

        review1.setId(id1);
        review1.setRating(2);

        Tour tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(4.5), tour.getAvgRating());
        assertEquals(2, tour.getVotesCount());

        review1.setRating(1);
        reviewDao.update(review1);

        tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(3.0), tour.getAvgRating());
        assertEquals(2, tour.getVotesCount());
    }

    @Test
    public void testDeleteGivenOneRecord() {
        Review review = TestData.getReview(data.user1, data.tour);

        Long id = reviewDao.create(review);
        assertNotNull(id);
        review.setId(id);

        Tour tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(4.0), tour.getAvgRating());
        assertEquals(1, tour.getVotesCount());

        reviewDao.delete(id);

        tour = tourDao.read(data.tour.getId());
        assertNull(tour.getAvgRating());
        assertEquals(0, tour.getVotesCount());
    }

    @Test
    public void testDeleteGivenTwoRecords() {
        Review review = TestData.getReview(data.user1, data.tour);

        Long id1 = reviewDao.create(review);
        review.setId(id1);
        assertNotNull(id1);

        review = TestData.getReview(data.user2, data.tour);
        review.setRating(1);
        Long id2 = reviewDao.create(review);
        assertNotNull(id2);

        Tour tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(2.5), tour.getAvgRating());
        assertEquals(2, tour.getVotesCount());

        reviewDao.delete(id1);

        tour = tourDao.read(data.tour.getId());
        assertEquals(new Double(1), tour.getAvgRating());
        assertEquals(1, tour.getVotesCount());

        reviewDao.delete(id2);
        tour = tourDao.read(data.tour.getId());
        assertNull(tour.getAvgRating());
        assertEquals(0, tour.getVotesCount());
    }

    @Test
    public void testFindAll() {
        Review review1 = TestData.getReview(data.user1, data.tour);
        Review review2 = TestData.getReview(data.user2, data.tour);

        Long id1 = reviewDao.create(review1);
        Long id2 = reviewDao.create(review2);

        assertNotNull(id1);
        assertNotNull(id2);

        review1.setId(id1);
        review2.setId(id2);

        List<Review> reviews = reviewDao.findAll();
        assertWeakEquals(review1, reviews.get(0));
        assertWeakEquals(review2, reviews.get(1));
    }

    private static void assertWeakEquals(Review expected, Review actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getRating(), actual.getRating());
        assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        assertEquals(expected.getTour().getId(), actual.getTour().getId());
    }


}
