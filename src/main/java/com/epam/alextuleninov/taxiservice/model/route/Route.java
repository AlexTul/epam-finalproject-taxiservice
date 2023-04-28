package com.epam.alextuleninov.taxiservice.model.route;

import com.epam.alextuleninov.taxiservice.model.route.adress.Address;

import java.util.Objects;

/**
 * Class for Route entity.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class Route {

    private long id;

    private Address address;

    private long distance;

    private double price;

    private int travelTime;

    public Route(long id, Address address, long distance, double price, int travelTime) {
        this.id = id;
        this.address = address;
        this.distance = distance;
        this.price = price;
        this.travelTime = travelTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id == route.id && Objects.equals(address, route.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }
}
