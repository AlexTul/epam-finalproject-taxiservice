package com.epam.alextuleninov.taxiservice.dao.order;

import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.model.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Interface DAO for Order.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface OrderDAO {

    /**
     * Create the order in the database.
     *
     * @param request       request with order parameters
     * @return              created order from database
     */
    Order create(OrderRequest request);

    /**
     * Find all orders from the database with pagination information.
     *
     * @param pageable      pageable with pagination information
     * @return              all orders from database with pagination information
     */
    List<Order> findAll(PageableRequest pageable);

    /**
     * Find all orders by range from the database.
     *
     * @param request       request with order`s parameters
     * @return              all users by range from database
     */
    List<Order> findAllByRange(OrderRequest request);

    /**
     * Find all orders by customer from the database.
     *
     * @param customer      customer from request
     * @param pageable      request with pagination information
     * @return              all users by range from database
     */
    List<Order> findAllByCustomer(String customer, PageableRequest pageable);

    /**
     * Find all orders by date start order from the database.
     *
     * @param startedAt     trip start date and time
     * @param pageable      request with pagination information
     * @return              all users by range from database
     */
    List<Order> findAllByDate(LocalDateTime startedAt, PageableRequest pageable);

    /**
     * Find order by id from the database.
     *
     * @return              order by id from database in response format
     */
    Optional<Order> findById(long id);

    /**
     * Find all dates by start order from the database.
     *
     * @return              all dates by start order from database
     */
    Set<LocalDateTime> findAllStartedAtDatesFromOrder();

    /**
     * Find number of records from the database.
     *
     * @return              number of record in database
     */
    long findNumberRecords();

    /**
     * Find number of records from the database by customer.
     *
     * @param customer      customer from request
     * @return              number of record in database
     */
    long findNumberRecordsByCustomer(String customer);

    /**
     * Find number of records from the database by date start order.
     *
     * @param startedAt     trip start date and time
     * @return              number of record in database
     */
    long findNumberRecordsByDateStartedAt(LocalDateTime startedAt);

    /**
     * Find sum order`s cost by customer from the database.
     *
     * @param request       request with order`s parameters
     * @return              sum order`s cost by customer
     */
    double sumCostByCustomer(OrderRequest request);

    /**
     * Update the order from database.
     *
     * @param id            id of order
     * @param orderRequest  request with parameter
     */
    void updateById(long id, OrderRequest orderRequest);

    /**
     * Delete the category in the database.
     *
     * @param id            id of order
     */
    void deleteById(long id);
}
