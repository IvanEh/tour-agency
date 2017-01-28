package com.gmail.at.ivanehreshi.epam.touragency.controller;

import com.gmail.at.ivanehreshi.epam.touragency.dispatcher.*;
import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.*;
import com.gmail.at.ivanehreshi.epam.touragency.service.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

public final class ToursController extends Controller {
    private TourDao tourDao = ServiceLocator.INSTANCE.get(TourDao.class);

    private TourService tourService = ServiceLocator.INSTANCE.get(TourService.class);

    private static final int MAX_DESC_LENGTH = 128;

    private static final int FILTER_MAX_PRICE = 9999;

    @Override
    public void get(RequestService reqService) {
        ToursDynamicFilter filter = prepareFilter(reqService);

        TourType[] tourTypesArr  = Stream.of(reqService.getString("type").split(","))
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .map(TourType::valueOf)
                .collect(Collectors.toList())
                .toArray(new TourType[]{});
        filter.setTourTypes(tourTypesArr);

        List<Tour> tours = tourService.executeDynamicFilter(filter);

        reqService.putParameter("tours", tours);
        reqService.putParameter("data", filter);

        for(TourType t: tourTypesArr) {
            reqService.putParameter(t.name(), true);
        }
    }

    private String shrinkText(String text) {
        if (text.length() > MAX_DESC_LENGTH) {
            return text.substring(0, MAX_DESC_LENGTH) + "...";
        }

        return text;
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

    private ToursDynamicFilter prepareFilter(RequestService reqService) {
        ToursDynamicFilter filter = new ToursDynamicFilter();

        SortDir priceOrd = reqService.getParameter("price")
                .map(String::toUpperCase)
                .flatMap(s -> TryOptionalUtil.of(() -> SortDir.valueOf(s)))
                .orElse(null);


        String searchStr = UrlParamDecoder.decode(reqService.getParameter("search").orElse(null))
                .orElse(null);

        Integer priceLow = reqService.getInt("priceLow").filter(p -> p != 0)
                .orElse(null);
        Integer priceHigh = reqService.getInt("priceHigh").filter(p -> p != FILTER_MAX_PRICE)
                .orElse(null);
        Boolean hot = reqService.getParameter("hot").map(p -> p.equals("on"))
                .orElse(null);

        filter.setPriceSort(priceOrd).setSearchQuery(searchStr)
                .setPriceLow(priceLow).setPriceHigh(priceHigh)
                .setHot(hot);
        return filter;
    }
}
