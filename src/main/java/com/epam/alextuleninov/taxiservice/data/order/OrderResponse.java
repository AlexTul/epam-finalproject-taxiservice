package com.epam.alextuleninov.taxiservice.data.order;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import com.epam.alextuleninov.taxiservice.model.order.Order;

import java.util.List;

/**
 * Class for the OrderResponse.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class OrderResponse {

    private long id;
    private String createdAt;
    private long customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerPassword;
    private String customerRole;
    private int numberOfPassengers;
    private String stringOfCars;
    private String startTravel;
    private String endTravel;
    private double travelDistance;
    private int travelDuration;
    private double cost;
    private String startedAt;
    private String finishedAt;

    public OrderResponse(long id, String createdAt, long customerId, String customerFirstName,
                         String customerLastName, String customerEmail, String customerPassword,
                         String customerRole, int numberOfPassengers, String stringOfCars,
                         String startTravel, String endTravel, double travelDistance,
                         int travelDuration, double cost, String startedAt, String finishedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
        this.customerRole = customerRole;
        this.numberOfPassengers = numberOfPassengers;
        this.stringOfCars = stringOfCars;
        this.startTravel = startTravel;
        this.endTravel = endTravel;
        this.travelDistance = travelDistance;
        this.travelDuration = travelDuration;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCustomerRole() {
        return customerRole;
    }

    public void setCustomerRole(String customerRole) {
        this.customerRole = customerRole;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getStringOfCars() {
        return stringOfCars;
    }

    public void setStringOfCars(String stringOfCars) {
        this.stringOfCars = stringOfCars;
    }

    public String getStartTravel() {
        return startTravel;
    }

    public void setStartTravel(String startTravel) {
        this.startTravel = startTravel;
    }

    public String getEndTravel() {
        return endTravel;
    }

    public void setEndTravel(String endTravel) {
        this.endTravel = endTravel;
    }

    public double getTravelDistance() {
        return travelDistance;
    }

    public void setTravelDistance(double travelDistance) {
        this.travelDistance = travelDistance;
    }

    public int getTravelDuration() {
        return travelDuration;
    }

    public void setTravelDuration(int travelDuration) {
        this.travelDuration = travelDuration;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }

    /**
     * Create a new object for OrderRequest given the selected locale.
     *
     * @param order             the order
     * @return                  new object
     */
    public static OrderResponse fromOrder(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCreatedAt().format(Constants.FORMATTER),
                order.getCustomer().getId(),
                order.getCustomer().getFirstName(),
                order.getCustomer().getLastName(),
                order.getCustomer().getEmail(),
                order.getCustomer().getPassword(),
                order.getCustomer().getRole().toString(),
                order.getNumberOfPassengers(),
                getStringOfCars(order.getCars()),
                order.getStartTravel(),
                order.getEndTravel(),
                order.getTravelDistance(),
                order.getTravelDuration(),
                order.getCost(),
                order.getStartedAt().format(Constants.FORMATTER),
                order.getFinishedAt().format(Constants.FORMATTER)
        );
    }

    /**
     * Get string of cars from order.
     *
     * @param cars list with cars
     * @return string with car`s name
     */
    private static String getStringOfCars(List<Car> cars) {
        var stringListOfCars = new StringBuilder();
        for (Car variable : cars) {
            stringListOfCars.append(variable.getCarName()).append(", ").append(variable.getCarCategory()).append("; ");
        }
        return stringListOfCars.toString();
    }
}
