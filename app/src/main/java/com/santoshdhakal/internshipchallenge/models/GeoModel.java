package com.santoshdhakal.internshipchallenge.models;

import android.arch.persistence.room.Embedded;

public class GeoModel {
    private double lat;

    private double lng;

    @Embedded
    private GeoModel geo;

    public GeoModel(double lat, double lng, GeoModel geo) {
        this.lat = lat;
        this.lng = lng;
        this.geo = geo;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public GeoModel getGeo() {
        return geo;
    }

    public void setGeo(GeoModel geo) {
        this.geo = geo;
    }
}
