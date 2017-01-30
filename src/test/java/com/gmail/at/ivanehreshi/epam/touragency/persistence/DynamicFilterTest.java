package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class DynamicFilterTest {
    private ConnectionManager connectionManager;
    private TourDao tourDao;
    private ToursDynamicFilter filter;
    private Tour tour1;
    private Tour tour2;
    private Tour tour3;

    @Before
    public void setUp() throws Exception {
        connectionManager = H2Db.init("database.sql");
        tourDao = new TourJdbcDao(connectionManager);
        filter = new ToursDynamicFilter();
        setUpData();
    }

    public void setUpData() {
        tour1 = TestData.getExcursionTour();
        tour1.setHot(true);
        Long id = tourDao.create(tour1);
        tour1.setId(id);

        tour2 = TestData.getShoppingTour();
        tour2.setTitle("abc def");
        id = tourDao.create(tour2);
        tour2.setId(id);

        tour3 = TestData.getRecreationTour();
        tour3.setDescription("__abc fds");
        id = tourDao.create(tour3);
        tour3.setId(id);
    }

    @After
    public void tearDown() throws Exception {
        new JdbcTemplate(connectionManager).executeSqlFile(
                ResourcesUtil.getResourceFile("clear-database.sql"));
    }

    @Test
    public void testFilterHot() {
        filter.setHotFirst(true);
        List<Tour> tours = tourDao.executeDynamicFilter(filter);
        assertEquals(1, tours.size());
        assertEquals(tour1, tours.get(0));
    }

    @Test
    public void testPriceLow() {
        filter.setPriceLow(50);
        List<Tour> tours = tourDao.executeDynamicFilter(filter);
        assertEquals(2, tours.size());
        assertEquals(tour1, tours.get(0));
        assertEquals(tour3, tours.get(1));
    }

    @Test
    public void testPriceLowHigh() {
        filter.setPriceLow(50).setPriceHigh(99);
        List<Tour> tours = tourDao.executeDynamicFilter(filter);
        assertEquals(1, tours.size());
        assertEquals(tour3, tours.get(0));
    }

    @Test
    public void testMultipleTourTypes() {
        filter.setTourTypes(TourType.EXCURSION, TourType.RECREATION);
        List<Tour> tours = tourDao.executeDynamicFilter(filter);
        assertEquals(2, tours.size());
        assertEquals(tour1, tours.get(0));
        assertEquals(tour3, tours.get(1));
    }

    @Test
    public void testSearchQueryMatchTitle() {
        filter.setSearchQuery("def");
        List<Tour> tours = tourDao.executeDynamicFilter(filter);
        assertEquals(1, tours.size());
        assertEquals(tour2, tours.get(0));
    }

    @Test
    public void testSearchQueryMatchTitleAndDescription() {
        filter.setSearchQuery("abc");
        List<Tour> tours = tourDao.executeDynamicFilter(filter);
        assertEquals(2, tours.size());
        assertEquals(tour2, tours.get(0));
        assertEquals(tour3, tours.get(1));
    }

    @Test
    public void testPriceSortDesc() {
        filter.setPriceSort(SortDir.DESC);
        List<Tour> tours = tourDao.executeDynamicFilter(filter);
        assertEquals(3, tours.size());
        assertEquals(tour1, tours.get(0));
        assertEquals(tour3, tours.get(1));
        assertEquals(tour2, tours.get(2));
    }

    @Test
    public void testPriceSortAsc() {
        filter.setPriceSort(SortDir.ASC);
        List<Tour> tours = tourDao.executeDynamicFilter(filter);
        assertEquals(3, tours.size());
        assertEquals(tour1, tours.get(2));
        assertEquals(tour3, tours.get(1));
        assertEquals(tour2, tours.get(0));
    }
}
