package com.epam.alextuleninov.taxiservice.model.car;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Class for Car entity.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class Car implements Comparable {

    private int id;

    private String carName;

    private int numberOfPassengers;

    private String carCategory;

    private String carStatus;

    public Car(int id, String carName, int numberOfPassengers, String carCategory, String carStatus) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return id == car.id && Objects.equals(carName, car.carName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carName);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.getId() - ((Car) o).getId();
    }
}
