package com.epam.alextuleninov.taxiservice.model.order;

import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.route.Route;
import com.epam.alextuleninov.taxiservice.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Class for Order entity.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class Order {

    private long id;
    private LocalDateTime createdAt;
    private User customer;
    private int numberOfPassengers;
    List<Car> cars;
    private Route route;
    private double cost;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    public Order() {
    }

    public Order(long id, LocalDateTime createdAt, User customer,
                 int numberOfPassengers, List<Car> cars, Route route,
                 double cost, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.customer = customer;
        this.numberOfPassengers = numberOfPassengers;
        this.cars = cars;
        this.route = route;
        this.cost = cost;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer);
    }
}
