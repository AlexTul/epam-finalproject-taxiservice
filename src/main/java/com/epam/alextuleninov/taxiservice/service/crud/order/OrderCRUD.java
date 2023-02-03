package com.epam.alextuleninov.taxiservice.service.crud.order;

import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.model.order.Order;

import java.util.List;

/**
 * Interface CRUD for Order.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public interface OrderCRUD {

    /**
     * Create the order in the database.
     *
     * @param request       request with order parameters
     * @return              the created order from database
     */
    Order create(OrderRequest request);

    /**
     * Find all orders from the database with pagination information.
     *
     * @param pageable      pageable with pagination information
     * @return              all orders from database with pagination information in response format
     */
    List<OrderResponse> findAll(PageableRequest pageable, String locale);

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
     * @param request       request with order`s parameters
     * @param pageable      request with pagination information
     * @return              all users by range from database in response format
     */
    List<OrderResponse> findAllByCustomer(OrderRequest request, PageableRequest pageable, String locale);

    /**
     * Find all orders by date start order from the database.
     *
     * @param request       request with order`s parameters
     * @param pageable      request with pagination information
     * @return              all users by range from database in response format
     */
    List<OrderResponse> findAllByDate(OrderRequest request, PageableRequest pageable, String locale);

    /**
     * Find all dates by start order from the database.
     *
     * @return              all dates by start order from database
     */
    List<String> findAllStartedAtDatesFromOrder();

    /**
     * Find number of records from the database.
     *
     * @return              number of record in database
     */
    long findNumberRecords();

    /**
     * Find number of records from the database by customer.
     *
     * @param request       request with order`s parameters
     * @return              number of record in database
     */
    long findNumberRecordsByCustomer(OrderRequest request);

    /**
     * Find number of records from the database by date start order.
     *
     * @param request       request with order`s parameters
     * @return              number of record in database
     */
    long findNumberRecordsByDateStartedAt(OrderRequest request);

    /**
     * Find sum order`s cost by customer from the database.
     *
     * @param request       request with order`s parameters
     * @return              sum order`s cost by customer
     */
    double sumCostByCustomer(OrderRequest request);
}
