package com.example.mapbox;

public class LocationO {
    private double latitude;
    private double longtidute;

    public LocationO(double latitude, double longtidute) {
        this.latitude = latitude;
        this.longtidute = longtidute;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtidute() {
        return longtidute;
    }

    public void setLongtidute(double longtidute) {
        this.longtidute = longtidute;
    }
}
