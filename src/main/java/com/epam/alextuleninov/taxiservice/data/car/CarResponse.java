package com.epam.alextuleninov.taxiservice.data.car;

import com.epam.alextuleninov.taxiservice.model.car.Car;

public class CarResponse {

    private int id;

    private String carName;

    private int numberOfPassengers;

    private String carCategory;

    private String carStatus;

    public CarResponse(int id, String carName, int numberOfPassengers, String carCategory, String carStatus) {
        this.id = id;
        this.carName = carName;
        this.numberOfPassengers = numberOfPassengers;
        this.carCategory = carCategory;
        this.carStatus = carStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }

    public String getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(String carCategory) {
        this.carCategory = carCategory;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    /**
     * Create the new class from Car.
     *
     * @param car           user entity
     * @return              class from car
     */
    public static CarResponse fromCar(Car car) {
        return new CarResponse(
                car.getId(),
                car.getCarName(),
                car.getNumberOfPassengers(),
                car.getCarCategory(),
                car.getCarStatus()
        );
    }
}
