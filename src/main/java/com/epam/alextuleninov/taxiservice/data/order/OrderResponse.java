package com.epam.alextuleninov.taxiservice.data.order;

import com.epam.alextuleninov.taxiservice.Constants;
import com.epam.alextuleninov.taxiservice.model.order.Order;

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
    private long routeId;
    private long addressesId;
    private String startEndLocale;
    private long routeDistance;
    private double routePrice;
    private int routeTravelTime;
    private double cost;
    private String startedAt;
    private String finishedAt;

    public OrderResponse() {
    }

    public OrderResponse(long id, String createdAt, long customerId, String customerFirstName,
                         String customerLastName, String customerEmail, String customerPassword,
                         String customerRole, int numberOfPassengers, long routeId, long addressesId,
                         String startEndLocale, long routeDistance, double routePrice,
                         int routeTravelTime, double cost, String startedAt, String finishedAt) {
        this.id = id;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
        this.customerRole = customerRole;
        this.numberOfPassengers = numberOfPassengers;
        this.routeId = routeId;
        this.addressesId = addressesId;
        this.startEndLocale = startEndLocale;
        this.routeDistance = routeDistance;
        this.routePrice = routePrice;
        this.routeTravelTime = routeTravelTime;
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

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public long getAddressesId() {
        return addressesId;
    }

    public void setAddressesId(long addressesId) {
        this.addressesId = addressesId;
    }

    public String getStartEndLocale() {
        return startEndLocale;
    }

    public void setStartEndLocale(String startEnd) {
        this.startEndLocale = startEnd;
    }

    public long getRouteDistance() {
        return routeDistance;
    }

    public void setRouteDistance(long routeDistance) {
        this.routeDistance = routeDistance;
    }

    public double getRoutePrice() {
        return routePrice;
    }

    public void setRoutePrice(double routePrice) {
        this.routePrice = routePrice;
    }

    public int getRouteTravelTime() {
        return routeTravelTime;
    }

    public void setRouteTravelTime(int routeTravelTime) {
        this.routeTravelTime = routeTravelTime;
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
     * @param locale            the locale
     * @return                  new object
     */
    public static OrderResponse fromOrder(Order order, String locale) {
        String startEndLocale;
        if (locale.equals("uk_UA")) {
            startEndLocale = order.getRoute().getAddress().startEndUk();
        } else {
            startEndLocale = order.getRoute().getAddress().startEnd();
        }

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
                order.getRoute().getId(),
                order.getRoute().getAddress().id(),
                startEndLocale,
                order.getRoute().getDistance(),
                order.getRoute().getPrice(),
                order.getRoute().getTravelTime(),
                order.getCost(),
                order.getStartedAt().format(Constants.FORMATTER),
                order.getFinishedAt().format(Constants.FORMATTER)
        );
    }
}
