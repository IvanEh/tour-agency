package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

public final class ToursController extends Controller {
    private TourDao tourDao = ServiceLocator.INSTANCE.get(TourDao.class);

    private TourService tourService = ServiceLocator.INSTANCE.get(TourService.class);

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
        WithStatus<Tour> tourWithStatus = extractTour(reqService);

        if (tourWithStatus.isOk()) {
            tourService.create(tourWithStatus.getPayload());
            reqService.redirect("/agent/tours.html");
        } else {
            reqService.redirect("/agent/new-tour.html?failed=true");
            reqService.putFlashParameter("tour", tourWithStatus.getPayload());
        }
    }

    @Override
    public void put(RequestService reqService) {
        WithStatus<Tour> tourWithStatus = extractTour(reqService);
        Long id = tourWithStatus.getPayload().getId();

        if (tourWithStatus.isOk()) {
            tourDao.update(tourWithStatus.getPayload());
            reqService.redirect("/agent/tours.html");
        } else {
            reqService.redirect("/agent/edit-tour.html?failed=true&id=" + id);
        }
    }

    private WithStatus<Tour> extractTour(RequestService reqService) {
        Tour tour = new Tour();

        tour.setId(reqService.getLong("id").orElse(null));
        tour.setTitle(reqService.getString("title"));
        tour.setDescription(reqService.getString("description"));
        tour.setType(TourType.values()[reqService.getInt("type").get()]);
        tour.setHot(reqService.getBool("hot").orElse(false));
        tour.setEnabled(reqService.getBool("enabled").orElse(true));

        try {
            tour.setPrice(new BigDecimal(reqService.getString("price")));
        } catch (NumberFormatException e) {
            return WithStatus.bad(tour);
        }

        return WithStatus.ok(tour);
    }
}
