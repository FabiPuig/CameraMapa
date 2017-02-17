package com.example.a20464654j.cameramapa;

/**
 * Created by 20464654j on 17/02/17.
 */

public class Gallery {

    private String absolute;
    private double lat;
    private double lon;

    public Gallery() {
    }

    public Gallery( String absolute, double lat, double lon) {
        this.absolute = absolute;
        this.lat = lat;
        this.lon = lon;
    }

    public String getAbsolute() {
        return absolute;
    }
}
