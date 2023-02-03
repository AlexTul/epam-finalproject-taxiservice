package com.epam.alextuleninov.taxiservice.dao.car;

import com.epam.alextuleninov.taxiservice.dao.mappers.ResultSetMapper;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.exceptions.datasource.UnexpectedDataAccessException;
import com.epam.alextuleninov.taxiservice.model.car.Car;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * Find all cars by category status from the database.
     *
     * @param request       request with order`s parameters
     * @return              all cars from the database
     */
    @Override
    public List<Car> findAllByCategoryStatus(OrderRequest request) {
        List<Car> result = new ArrayList<>();

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
     * Change car status int the database.
     *
     * @param request       request with order`s parameters
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
}
