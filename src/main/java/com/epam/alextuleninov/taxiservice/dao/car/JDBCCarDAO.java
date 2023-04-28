package com.epam.alextuleninov.taxiservice.dao.car;

import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.data.car.CarRequest;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * Class DAO for Car.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class JDBCCarDAO implements CarDAO {

    private final DataSource dataSource;
    private final ResultSetMapper<Car> mapper;

    public JDBCCarDAO(DataSource dataSource, ResultSetMapper<Car> mapper) {
        this.dataSource = dataSource;
        this.mapper = mapper;
    }

    /**
     * Create the car in the database.
     *
     * @param request request with order parameters
     * @return the created car from database
     */
    @Override
    public Car create(CarRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (var createCar = connection.prepareStatement(
                    """
                            insert into cars (car_name, car_passengers, car_category, car_status)
                            values (?, ?, ?, ?)
                            """,
                    Statement.RETURN_GENERATED_KEYS)) {

                createCar.setString(1, request.carName());
                createCar.setInt(2, request.numberOfPassengers());
                createCar.setString(3, request.carCategory());
                createCar.setString(4, request.carStatus());

                createCar.executeUpdate();

                ResultSet generatedKeys = createCar.getGeneratedKeys();
                generatedKeys.next();
                int id = generatedKeys.getInt(1);

                connection.commit();

                return new Car(
                        id,
                        request.carName(),
                        request.numberOfPassengers(),
                        request.carCategory(),
                        request.carStatus()
                );
            } catch (Exception e) {
                connection.rollback();
                throw new UnexpectedDataAccessException(e);
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Find all cars from the database with pagination.
     *
     * @return all cars from database
     */
    @Override
    public Set<Car> findAll(PageableRequest pageable) {
        Set<Car> result = new TreeSet<>();

        String sql = "select * from cars c " +
                " order by c." + pageable.sortField() + " " + pageable.orderBy() +
                " limit " + pageable.limit() + " offset " + pageable.offset();

        try (Connection connection = dataSource.getConnection()) {
            try (var preparedStatement = connection.prepareStatement(
                    sql
            )) {

                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
            }
        } catch (NullPointerException e) {
            return result;
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return result;
    }

    /**
     * Find all cars by category status from the database.
     *
     * @param request request with order`s parameters
     * @return all cars from the database
     */
    @Override
    public Set<Car> findAllByCategoryStatus(OrderRequest request) {
        Set<Car> result = new TreeSet<>();

        try (Connection connection = dataSource.getConnection()) {

            try (var getAllCarByCategory = connection.prepareStatement(
                    """
                            select * from cars c
                            where c.car_category like ? and c.car_status like ?
                                                        """
            )) {
                getAllCarByCategory.setString(1, request.carCategory());
                getAllCarByCategory.setString(2, request.carStatus());

                ResultSet resultSet = getAllCarByCategory.executeQuery();
                while (resultSet.next()) {
                    result.add(mapper.map(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
        return result;
    }

    /**
     * Find car by ID from the database.
     *
     * @param id id of car
     * @return car from the database
     */
    @Override
    public Optional<Car> findByID(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try (var psGetCar = connection.prepareStatement(
                    """
                            select * from cars c
                            where c.car_id = ?
                            """
            )) {

                psGetCar.setInt(1, id);

                ResultSet resultSet = psGetCar.executeQuery();
                if (resultSet.next()) {
                    return Optional.of(mapper.map(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Change car status int the database.
     *
     * @param request request with order`s parameters
     */
    @Override
    public void changeCarStatus(OrderRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (var bookCars = connection.prepareStatement(
                    """
                            update cars c
                            set car_status = ? where c.car_id = ?
                            """)) {

                for (int i = 0; i < request.cars().size(); i++) {
                    bookCars.setString(1, request.carStatus());
                    bookCars.setInt(2, request.cars().get(i).getId());

                    bookCars.executeUpdate();
                }

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new UnexpectedDataAccessException(e);
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Find number of records from the database.
     *
     * @return number of record in database
     */
    @Override
    public long findNumberRecords() {
        try (Connection connection = dataSource.getConnection()) {
            try (var getNumberRecords = connection.prepareStatement(
                    """
                            select count(c.car_id) as result from cars c
                            """
            )) {

                ResultSet resultSet = getNumberRecords.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                } else {
                    throw new UnexpectedDataAccessException("Number of cars records not found");
                }
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Update the car from database.
     *
     * @param id      id of car
     * @param request request with parameter
     */
    @Override
    public void updateByID(int id, CarRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (var updateCarByID = connection.prepareStatement(
                    """
                            update cars c
                            set car_name = ?, car_passengers = ?, car_category = ?, car_status = ?
                            where car_id = ?
                            """
            )) {

                updateCarByID.setString(1, request.carName());
                updateCarByID.setInt(2, request.numberOfPassengers());
                updateCarByID.setString(3, request.carCategory());
                updateCarByID.setString(4, request.carStatus());
                updateCarByID.setInt(5, id);

                updateCarByID.executeUpdate();

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new UnexpectedDataAccessException(e);
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }

    /**
     * Delete the car from database.
     *
     * @param id id of car
     */
    @Override
    public void deleteByID(int id) {
        try (Connection connection = dataSource.getConnection()) {
            try (var psFindOrders = connection.prepareStatement(
                    """
                            select * from orders o
                            join order_car oc on o.id = oc.o_id
                            where oc.c_id = ?
                            """
            );
                 var psDeleteOrders = connection.prepareStatement(
                         """
                                 delete from orders o
                                 where o.id = ?
                                 """
                 );
                 var psDeleteCars = connection.prepareStatement(
                         """
                                 delete from cars c
                                 where c.car_id = ?
                                 """
                 )) {

                // find orders by car`s id
                List<Long> result = new ArrayList<>();
                psFindOrders.setLong(1, id);
                ResultSet resultSet = psFindOrders.executeQuery();
                while (resultSet.next()) {
                    result.add(resultSet.getLong(1));
                }

                // delete orders by id
                for (Long aLong : result) {
                    psDeleteOrders.setLong(1, aLong);
                    psDeleteOrders.executeUpdate();
                }

                // delete car by id
                psDeleteCars.setInt(1, id);
                psDeleteCars.executeUpdate();
            }
        } catch (SQLException e) {
            throw new UnexpectedDataAccessException(e);
        }
    }
}
