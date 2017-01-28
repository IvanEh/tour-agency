package com.gmail.at.ivanehreshi.epam.touragency.service;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.impl.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class TourServiceTest {
    private ConnectionManager connectionManager;
    private TourService tourService;

    @Before
    public void setUp() throws Exception {
        connectionManager = H2Db.init("database.sql");
        tourService = new TourServiceImpl(new TourJdbcDao(connectionManager));
    }

    @After
    public void tearDown() throws Exception {
        new JdbcTemplate(connectionManager).executeSqlFile(
                ResourcesUtil.getResourceFile("clear-database.sql"));
    }

    @Test
    public void testCreateReadById() {
        Tour tour1 = TestData.getExcursionTour();
        tourService.create(tour1);

        Tour tour2 = tourService.read(tour1.getId());
        assertEquals(tour1, tour2);
    }

    @Test
    public void testUpdate() {
        Tour tour1 = TestData.getExcursionTour();

        tourService.create(tour1);

        String old = tour1.getDescription();
        tour1.setDescription("SOME DESCRIPTION");

        tourService.update(tour1);

        Tour tour2 = tourService.read(tour1.getId());
        assertEquals(tour1, tour2);
    }

    @Test
    public void testFindAll() {
        Tour tour1 = TestData.getExcursionTour();
        Tour tour2 = TestData.getShoppingTour();

        tourService.create(tour1);
        tourService.create(tour2);

        assertEquals(Arrays.asList(tour2, tour1), tourService.findAll());
    }

    @Test
    public void testFindRandomTours() {
        Tour tour1 = TestData.getExcursionTour();
        tour1.setHot(true);
        Tour tour2 = TestData.getShoppingTour();

        tourService.create(tour1);
        tourService.create(tour2);

        Set<Tour> hots = tourService.findRandomHotTours(1);
        assertEquals(1, hots.size());
        assertTrue(hots.contains(tour1));
    }

    @Test
    public void testFindRandomToursWhenLessThenNeeded() {
        Tour tour1 = TestData.getExcursionTour();
        tour1.setHot(true);
        Tour tour2 = TestData.getShoppingTour();

        tourService.create(tour1);
        tourService.create(tour2);

        Set<Tour> hots = tourService.findRandomHotTours(42);
        assertEquals(1, hots.size());
        assertTrue(hots.contains(tour1));
    }
    @Test
    public void testDelete() {
        Tour tour1 = TestData.getExcursionTour();
        tourService.create(tour1);
        tourService.delete(tour1.getId());
        Tour tour2 = tourService.read(tour1.getId());

        assertNull(tour2);
    }

}
