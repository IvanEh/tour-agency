package com.gmail.at.ivanehreshi.epam.touragency.persistence;

import java.util.List;

public class Slice<P> {
    private List<P> payload;
    private P bottomAnchor;
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
