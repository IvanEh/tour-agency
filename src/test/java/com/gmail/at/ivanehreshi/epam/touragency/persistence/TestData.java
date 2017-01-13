package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.*;

import java.math.*;
import java.util.*;

public class TestData {
    public static User getUser() {
        User user = new User();
        user.setPassword("passw");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john");
        user.setDiscount(20);
        user.setRoles(Arrays.asList(Role.CUSTOMER));
        return user;
    }

    public static User getPrivilegedUser() {
        User user = getUser();
        user.setRoles(Arrays.asList(Role.CUSTOMER, Role.TOUR_AGENT));
        return user;
    }

    public static Tour getExcursionTour() {
        Tour tour = new Tour();
        tour.setEnabled(true);
        tour.setHot(false);
        tour.setPrice(new BigDecimal(100));
        tour.setDescription("NO DESC");
        tour.setTitle("Tour 1");
        tour.setType(TourType.EXCURSION);
        return tour;
    }

    public static Tour getShoppingTour() {
        Tour tour = new Tour();
        tour.setEnabled(true);
        tour.setHot(false);
        tour.setPrice(new BigDecimal(10));
        tour.setDescription("NO DESC");
        tour.setTitle("Tour 2");
        tour.setType(TourType.SHOPPING);
        return tour;
    }

    public static Tour getRecreationTour() {
        Tour tour = new Tour();
        tour.setEnabled(true);
        tour.setHot(false);
        tour.setPrice(new BigDecimal(50));
        tour.setDescription("NO DESC");
        tour.setTitle("Tour 3");
        tour.setType(TourType.RECREATION);
        return tour;
    }

    public static UserTestData getUserTestData(ConnectionManager cm) {
        return getUserTestData(cm, "");
    }

    public static UserTestData getUserTestData(ConnectionManager cm, String seed) {
        TourDao tourDao = new TourJdbcDao(cm);
        UserDao userDao = new UserJdbcDao(cm);

        Tour tour1 = TestData.getExcursionTour();
        tour1.setTitle(tour1.getTitle() + seed);
        Long id = tourDao.create(tour1);
        tour1.setId(id);

        Tour tour2 = TestData.getRecreationTour();
        tour2.setTitle(tour2.getTitle() + seed);
        id = tourDao.create(tour2);
        tour2.setId(id);

        User user = TestData.getUser();
        user.setUsername(user.getUsername() + seed);
        id = userDao.create(user);
        user.setId(id);

        return new UserTestData(tour1, tour2, user);
    }

    public static class UserTestData {
        public Tour tour1;
        public Tour tour2;
        public User user;

        public UserTestData(Tour tour1, Tour tour2, User user) {
            this.tour1 = tour1;
            this.tour2 = tour2;
            this.user = user;
        }
    }
}
