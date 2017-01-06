package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.Controller;
import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.RequestService;
import com.gmail.at.ivanehreshi.epam.touragency.domain.Tour;
import com.gmail.at.ivanehreshi.epam.touragency.domain.TourType;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.ScrollDirection;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.Slice;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.TourDao;
import com.gmail.at.ivanehreshi.epam.touragency.util.Ordering;
import com.gmail.at.ivanehreshi.epam.touragency.util.ServiceLocator;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ToursController extends Controller {
    private TourDao tourDao = ServiceLocator.INSTANCE.get(TourDao.class);

    private static final int PAGE_SIZE = 10;

    @Override
    public void get(RequestService reqService) {
        String priceOrdStr = reqService.getString("price");
        String tourTypesStr = reqService.getString("type");

        ScrollDirection dir = reqService.getInt("direction")
                .map(ScrollDirection::valueOf)
                .orElse(ScrollDirection.DOWN);


        Tour anchor = null;
        boolean hasCurrId = reqService.getString("currId") != null;
        if (hasCurrId && !reqService.getString("currId").isEmpty()) {
            anchor = new Tour(reqService.getLong("currId").orElse(null));
            anchor.setPrice(new BigDecimal(reqService.getString("currPrice")));
        }

        List<TourType> tourTypes = Stream.of(tourTypesStr.split(","))
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .map(TourType::valueOf)
                .collect(Collectors.toList());

        Ordering priceOrd = Ordering.NO;
        if (priceOrdStr != null) {
            try {
                priceOrd = Ordering.valueOf(priceOrdStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                priceOrd = Ordering.NO;
            }
        }

        TourType[] tourTypesArr = tourTypes.toArray(new TourType[]{});
        Slice<Tour> tours = tourDao.getToursSliceByCriteria(PAGE_SIZE, anchor,
                        dir, priceOrd, tourTypesArr);

        reqService.putParameter("tours",  tours.getPayload());

        for(TourType t: tourTypesArr) {
            reqService.putParameter(t.name(), true);
        }

        reqService.putParameter("ord", priceOrd.name());

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

        try {
            tour.setTitle(reqService.getString("title"));
            tour.setDescription(reqService.getString("description"));
            tour.setPrice(new BigDecimal(reqService.getString("price")));
            tour.setType(TourType.values()[reqService.getInt("type").get()]);
            tour.setHot(reqService.getBool("hot").orElse(false));
            tour.setEnabled(reqService.getBool("enabled").orElse(true));

            tourDao.create(tour);
            
            reqService.redirect("/agent/tours.html");
        } catch (NumberFormatException e) {
            reqService.redirect("/agent/new-tour.html?failed=true");
            reqService.putFlashParameter("tour", tour);
        }
    }

    @Override
    public void put(RequestService reqService) {
        Long id = reqService.getLong("id").orElse(null);
        try {
            Tour tour = new Tour();
            tour.setId(id);
            tour.setTitle(reqService.getString("title"));
            tour.setDescription(reqService.getString("description"));
            tour.setPrice(new BigDecimal(reqService.getString("price")));
            tour.setType(TourType.values()[reqService.getInt("type").get()]);
            tour.setHot(reqService.getBool("hot").orElse(false));
            tour.setEnabled(reqService.getBool("enabled").orElse(true));

            tourDao.update(tour);
            reqService.redirect("/agent/tours.html");
        } catch (NumberFormatException e) {
            reqService.redirect("/agent/edit-tour.html?failed=true&id=" + id);
        }
    }
}
