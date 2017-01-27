package com.gmail.at.ivanehreshi.epam.touragency.persistence.dao;

import com.gmail.at.ivanehreshi.epam.touragency.domain.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder.*;
import com.gmail.at.ivanehreshi.epam.touragency.persistence.query.condition.*;
import com.gmail.at.ivanehreshi.epam.touragency.util.*;

import java.util.*;
import java.util.stream.*;

import static com.gmail.at.ivanehreshi.epam.touragency.persistence.query.Ordering.*;
import static com.gmail.at.ivanehreshi.epam.touragency.persistence.query.builder.WhereBuilder.*;

public class ToursDynamicFilter {
    private String searchQuery;

    private Integer priceLow;

    private Integer priceHigh;

    private EnumSet<TourType> tourTypes;

    private Boolean hot;

    private SortDir priceSort;

    public ToursDynamicFilter() {
    }

    public ToursDynamicFilter setTourTypes(TourType... tourTypes) {
        if (tourTypes != null) {
            if (tourTypes.length == 0) {
                this.tourTypes = null;
            } else if (tourTypes.length == 1) {
                this.tourTypes = EnumSet.of(tourTypes[0]);
            } else {
                this.tourTypes = EnumSet.of(tourTypes[0], tourTypes);
            }
        }

        return this;
    }

    public ToursDynamicFilter setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
        return this;
    }

    public ToursDynamicFilter setPriceSort(SortDir priceSort) {
        this.priceSort = priceSort;
        return this;
    }

    public ToursDynamicFilter setPriceLow(Integer priceLow) {
        this.priceLow = priceLow;
        return this;
    }

    public ToursDynamicFilter setPriceHigh(Integer priceHigh) {
        this.priceHigh = priceHigh;
        return this;
    }

    public ToursDynamicFilter setHot(Boolean hot) {
        this.hot = hot;
        return this;
    }

    public String getQuery() {
        WhereBuilder where = QueryBuilder.select("tour", "*");

        where = buildPriceLow(where);
        where = buildPriceHigh(where);
        where = buildTourTypes(where);
        where = buildHot(where);
        where = buildSearch(where);
        OrderByBuilder orderBy = buildSortDir(where);

        return orderBy.build();
    }

    private WhereBuilder buildSearch(WhereBuilder where) {
        if (searchQuery != null) {
            List<String> keywords = Arrays.asList(searchQuery.split(" "));

            LikeCondition[] conds = keywords.stream()
                    .map(s -> "%" + s + "%")
                    .flatMap(s -> Stream.of(new LikeCondition("title", s),
                            new LikeCondition("description", s)))
                    .collect(Collectors.toList())
                    .toArray(new LikeCondition[]{});

            return where.and(or(conds));
        }
        return where;
    }

    private OrderByBuilder buildSortDir(WhereBuilder where) {
        if (priceSort != null) {
            return where.orderBy("price", priceSort);
        }
        return where;
    }

    private WhereBuilder buildHot(WhereBuilder where) {
        if (hot != null) {
            return where.and(rel("hot", EQ, hot ? 1 : 0));
        }
        return where;
    }

    private WhereBuilder buildTourTypes(WhereBuilder where) {
        if (tourTypes != null && !tourTypes.isEmpty()) {
            return where.and(in("type", false, tourTypes.stream()
                    .map(TourType::ordinal)
                    .map(String::valueOf)
                    .collect(Collectors.toList())
                    .toArray(new String[]{})));
        }

        return where;
    }

    private WhereBuilder buildPriceHigh(WhereBuilder where) {
        if (priceHigh != null) {
            return where.and(rel("price", LESSEQ, priceHigh));
        }
        return where;
    }

    private WhereBuilder buildPriceLow(WhereBuilder where) {
        if (priceLow != null) {
            return where.and(rel("price", GREATEREQ, priceLow));
        }
        return where;
    }

    public Boolean getHot() {
        return hot;
    }

    public Integer getPriceHigh() {
        return priceHigh;
    }

    public Integer getPriceLow() {
        return priceLow;
    }

    public SortDir getPriceSort() {
        return priceSort;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public EnumSet<TourType> getTourTypes() {
        return tourTypes;
    }
}
