package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import java.util.List;

/**
 * This class is planned for generalizing Database slicing and paging
 * Currently used only for TourDao as an example
 * @param <P> is an entity list of which this object contains
 *
 * @see com.gmail.at.ivanehreshi.epam.touragency.persistence.dao.jdbc.TourJdbcDao#getToursSliceByCriteria
 */
public class Slice<P> {
    private List<P> payload;

    /**
     * The last element of the slice. Used for retrieving the next slice
     */
    private P bottomAnchor;

    /**
     * The first element of the slice. Used for retrieving the previous slice
     */
    private P topAnchor;

    public Slice(List<P> payload, P topAnchor, P bottomAnchor) {
        this.payload = payload;
        this.topAnchor = topAnchor;
        this.bottomAnchor = bottomAnchor;
    }

    public P getBottomAnchor() {
        return bottomAnchor;
    }

    public void setBottomAnchor(P bottomAnchor) {
        this.bottomAnchor = bottomAnchor;
    }

    public P getTopAnchor() {
        return topAnchor;
    }

    public void setTopAnchor(P topAnchor) {
        this.topAnchor = topAnchor;
    }

    public List<P> getPayload() {
        return payload;
    }

    public void setPayload(List<P> payload) {
        this.payload = payload;
    }
}
