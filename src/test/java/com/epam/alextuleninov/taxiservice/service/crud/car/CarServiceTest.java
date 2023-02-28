package com.epam.alextuleninov.taxiservice.service.crud.car;

import com.epam.alextuleninov.taxiservice.dao.car.CarDAO;
import com.epam.alextuleninov.taxiservice.data.car.CarResponse;
import com.epam.alextuleninov.taxiservice.model.car.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static com.epam.alextuleninov.taxiservice.TestUtils.getTestOrderRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    private CarDAO carDAO;
    private CarCRUD carCRUD;

    @BeforeEach
    void setUp() {
        this.carDAO = mock(CarDAO.class);
        this.carCRUD = new CarService(carDAO);
    }

    @Test
    void testCreate() {
        when(carDAO.create(getTestCarRequest())).thenReturn(getTestCar());
        var carResponse = carCRUD.create(getTestCarRequest());

        assertEquals(CarResponse.fromCar(getTestCar()), carResponse);
    }

    @Test
    void testFindAll() {
        Set<Car> cars = new TreeSet<>();
        cars.add(getTestCar());

        when(carDAO.findAll(getTestPageableRequest())).thenReturn(cars);
        var resultPresent = carCRUD.findAll(getTestPageableRequest());

        assertNotNull(resultPresent);
        assertEquals(1, resultPresent.size());
        assertEquals(CarResponse.fromCar(getTestCar()), resultPresent.get(0));

        when(carDAO.findAll(getTestPageableRequest())).thenReturn(new TreeSet<>());
        var resultAbsent = carCRUD.findAll(getTestPageableRequest());

        assertEquals(0, resultAbsent.size());
    }

    @Test
    void testFindAllByCategoryStatus() {
        Set<Car> cars = new TreeSet<>();
        cars.add(getTestCar());

        when(carDAO.findAllByCategoryStatus(getTestOrderRequest())).thenReturn(cars);
        var resultPresent = carCRUD.findAllByCategoryStatus(getTestOrderRequest());

        assertNotNull(resultPresent);
        assertEquals(1, resultPresent.size());
        assertEquals(getTestCar(), resultPresent.stream().toList().get(0));

        when(carDAO.findAllByCategoryStatus(getTestOrderRequest())).thenReturn(new TreeSet<>());
        var resultAbsent = carCRUD.findAllByCategoryStatus(getTestOrderRequest());

        assertEquals(0, resultAbsent.size());
    }

    @Test
    void testFindByID() {
        // if value is present
        when(carDAO.findByID(0)).thenReturn(Optional.of(getTestCar()));
        Optional<CarResponse> resultPresent = carCRUD.findByID(0);

        assertNotNull(resultPresent);
        assertEquals(Optional.of(CarResponse.fromCar(getTestCar())), resultPresent);

        // if value is absent
        when(carDAO.findByID(0)).thenReturn(Optional.empty());
        Optional<CarResponse> resultAbsent = carCRUD.findByID(0);

        assertEquals(Optional.empty(), resultAbsent);
    }

    @Test
    void testChangeCarStatus() {
        doNothing().when(carDAO).changeCarStatus(getTestOrderRequest());
        assertDoesNotThrow(() -> carCRUD.changeCarStatus(getTestOrderRequest()));
    }

    @Test
    void testFindNumberRecords() {
        // if value is present
        when(carDAO.findNumberRecords()).thenReturn(1L);
        long resultPresent = carCRUD.findNumberRecords();

        assertEquals(1, resultPresent);

        // if value is absent
        when(carDAO.findNumberRecords()).thenReturn(0L);
        long resultAbsent = carCRUD.findNumberRecords();

        assertEquals(0, resultAbsent);
    }

    @Test
    void testUpdateByID() {
        doNothing().when(carDAO).updateByID(0, getTestCarRequest());
        assertDoesNotThrow(() -> carCRUD.updateByID(0, getTestCarRequest()));
    }

    @Test
    void testDeleteByID() {
        doNothing().when(carDAO).deleteByID(0);
        assertDoesNotThrow(() -> carCRUD.deleteByID(0));
    }
}
