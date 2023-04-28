package com.epam.alextuleninov.taxiservice.model.order;

import com.epam.alextuleninov.taxiservice.model.car.Car;
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

    private final long id;
    private final LocalDateTime createdAt;
    private final User customer;
    private final int numberOfPassengers;
    private final List<Car> cars;
    private final String startTravel;
    private final String endTravel;
    private final double travelDistance;
    private final int travelDuration;
    private final double cost;
    private final LocalDateTime startedAt;
    private final LocalDateTime finishedAt;

    private Order(long id, LocalDateTime createdAt, User customer, int numberOfPassengers,
                 List<Car> cars, String startTravel, String endTravel, double travelDistance,
                 int travelDuration, double cost, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.customer = customer;
        this.numberOfPassengers = numberOfPassengers;
        this.cars = cars;
        this.startTravel = startTravel;
        this.endTravel = endTravel;
        this.travelDistance = travelDistance;
        this.travelDuration = travelDuration;
        this.cost = cost;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public static Order create(long id, LocalDateTime createdAt, User customer, int numberOfPassengers,
                               List<Car> cars, String startTravel, String endTravel, double travelDistance,
                               int travelDuration, double cost, LocalDateTime startedAt, LocalDateTime finishedAt) {
        return new Order(id, createdAt, customer, numberOfPassengers, cars, startTravel,
                endTravel, travelDistance, travelDuration, cost, startedAt, finishedAt);
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getCustomer() {
        return customer;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public List<Car> getCars() {
        return cars;
    }

    public String getStartTravel() {
        return startTravel;
    }

    public String getEndTravel() {
        return endTravel;
    }

    public double getTravelDistance() {
        return travelDistance;
    }

    public int getTravelDuration() {
        return travelDuration;
    }

    public double getCost() {
        return cost;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
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

    public static class OrderBuilder {

        private long id;
        private LocalDateTime createdAt;
        private User customer;
        private int numberOfPassengers;
        private List<Car> cars;
        private String startTravel;
        private String endTravel;
        private double travelDistance;
        private int travelDuration;
        private double cost;
        private LocalDateTime startedAt;
        private LocalDateTime finishedAt;

        public OrderBuilder id(long id) {
            this.id = id;
            return this;
        }

        public OrderBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderBuilder customer(User customer) {
            this.customer = customer;
            return this;
        }

        public OrderBuilder numberOfPassengers(int numberOfPassengers) {
            this.numberOfPassengers = numberOfPassengers;
            return this;
        }

        public OrderBuilder cars(List<Car> cars) {
            this.cars = cars;
            return this;
        }

        public OrderBuilder startTravel(String startTravel) {
            this.startTravel = startTravel;
            return this;
        }

        public OrderBuilder endTravel(String endTravel) {
            this.endTravel = endTravel;
            return this;
        }

        public OrderBuilder travelDistance(double travelDistance) {
            this.travelDistance = travelDistance;
            return this;
        }

        public OrderBuilder travelDuration(int travelDuration) {
            this.travelDuration = travelDuration;
            return this;
        }

        public OrderBuilder cost(double cost) {
            this.cost = cost;
            return this;
        }

        public OrderBuilder startedAt(LocalDateTime startedAt) {
            this.startedAt = startedAt;
            return this;
        }

        public OrderBuilder finishedAt(LocalDateTime finishedAt) {
            this.finishedAt = finishedAt;
            return this;
        }

        public Order build() {
            return Order.create(id, createdAt, customer, numberOfPassengers, cars, startTravel,
                    endTravel, travelDistance, travelDuration, cost, startedAt, finishedAt);
        }
    }
}
