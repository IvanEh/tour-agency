package com.gmail.at.ivanehreshi.epam.touragency.command;

import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ScrollDirection;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.Slice;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.servlet.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.util.Ordering;
import com.gmail.at.ivanehreshi.epam.touragency.web.ObjectFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ToursController extends Controller {
    private TourDao tourDao = ObjectFactory.INSTANCE.get(TourDao.class);

    private static final int PAGE_SIZE = 10;

    @Override
    public void get(RequestService reqService) {
        String priceOrdStr = reqService.getString("price");
        String tourTypesStr = reqService.getString("type");
        Integer direction = reqService.getInt("direction");
        direction = direction == null ? 1 : direction;
        ScrollDirection dir = ScrollDirection.valueOf(direction);

        Tour anchor = null;
        if(reqService.getString("currId") != null && !reqService.getString("currId").isEmpty()) {
            anchor = new Tour(reqService.getLong("currId"));
            anchor.setPrice(new BigDecimal(reqService.getString("currPrice")));
        }

        tourTypesStr = tourTypesStr == null ? "" : tourTypesStr;
        List<TourType> tourTypes = new ArrayList<>();
        for(String t: tourTypesStr.split(",")) {
            try {
                tourTypes.add(TourType.valueOf(t.toUpperCase()));
            } catch (IllegalArgumentException e){}
        }

        Ordering priceOrd = Ordering.NO;
        if(priceOrdStr != null) {
            try {
                priceOrd = Ordering.valueOf(priceOrdStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                priceOrd = Ordering.NO;
            }
        }


        Slice<Tour> tours =
                tourDao.getToursSliceByCriteria(PAGE_SIZE, anchor, dir, priceOrd,
                                           (TourType[]) tourTypes.toArray(new TourType[]{}));

        reqService.putParameter("tours",  tours.getPayload());

        if(tours.getBottomAnchor() != null) {
            reqService.putParameter("nextId", tours.getBottomAnchor().getId());
            reqService.putParameter("nextPrice", tours.getBottomAnchor().getPrice());
        }

        if(tours.getTopAnchor() != null) {
            reqService.putParameter("prevId", tours.getTopAnchor().getId());
            reqService.putParameter("prevPrice", tours.getTopAnchor().getPrice());
        }
    }

    @Override
    public void post(RequestService reqService) {
        Tour tour = new Tour();
        tour.setTitle(reqService.getString("title"));
        tour.setDescription(reqService.getString("description"));
        tour.setPrice(new BigDecimal(reqService.getString("price")));
        tour.setType(TourType.values()[reqService.getInt("type")]);
        tour.setHot(reqService.getBool(""));
        tourDao.create(tour);

        reqService.redirect("/agent/tours.html");
    }

    @Override
    public void put(RequestService reqService) {
        Tour tour = new Tour();
        tour.setId(reqService.getLong("id"));
        tour.setTitle(reqService.getString("title"));
        tour.setDescription(reqService.getString("description"));
        tour.setPrice(new BigDecimal(reqService.getString("price")));
        tour.setType(TourType.values()[reqService.getInt("type")]);
        tour.setHot(reqService.getBool("hot"));

        tourDao.update(tour);

        reqService.redirect("/agent/tours.html");
    }
}
