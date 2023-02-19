package com.epam.alextuleninov.taxiservice.service.crud.order;

import com.epam.alextuleninov.taxiservice.dao.order.OrderDAO;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.model.order.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class CRUD for Order.
 *
 * @author Oleksandr Tuleninov
 * @version 01
 */
public class OrderService implements OrderCRUD {

    private final OrderDAO orderDAO;

    public OrderService(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    /**
     * Create the order in the database.
     *
     * @param request       request with order parameters
     * @return              the created order from database
     */
    @Override
    public OrderResponse create(OrderRequest request) {
        return OrderResponse.fromOrder(orderDAO.create(request));
    }

    /**
     * Find all orders from the database with pagination information.
     *
     * @param pageable      pageable with pagination information
     * @return              all orders from database with pagination information in response format
     */
    @Override
    public List<OrderResponse> findAll(PageableRequest pageable) {
        return orderDAO.findAll(pageable).stream()
                .map(OrderResponse::fromOrder)
                .toList();
    }

    /**
     * Find all orders by range from the database.
     *
     * @param request       request with order`s parameters
     * @return              all users by range from database
     */
    @Override
    public List<Order> findAllByRange(OrderRequest request) {
        return orderDAO.findAllByRange(request);
    }

    /**
     * Find all orders by customer from the database.
     *
     * @param customer      customer from request
     * @param pageable      request with pagination information
     * @return              all users by range from database in response format
     */
    @Override
    public List<OrderResponse> findAllByCustomer(String customer, PageableRequest pageable) {
        return orderDAO.findAllByCustomer(customer, pageable)
                .stream()
                .map(order -> OrderResponse.fromOrder(order))
                .toList();
    }

    /**
     * Find all orders by date start order from the database.
     *
     * @param startedAt     trip start date and time
     * @param pageable      request with pagination information
     * @return              all users by range from database in response format
     */
    @Override
    public List<OrderResponse> findAllByDate(LocalDateTime startedAt, PageableRequest pageable) {
        return orderDAO.findAllByDate(startedAt, pageable)
                .stream()
                .map(order -> OrderResponse.fromOrder(order))
                .toList();
    }

    /**
     * Find all dates by start order from the database.
     *
     * @return              all dates by start order from database
     */
    @Override
    public List<String> findAllStartedAtDatesFromOrder() {
        var listStartedAtDates = orderDAO.findAllStartedAtDatesFromOrder().stream()
                .map(LocalDateTime::toLocalDate)
                .map(LocalDate::toString)
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));
        listStartedAtDates.addAll(0, Arrays.asList("--------------------------", "all"));

        return listStartedAtDates;
    }

    /**
     * Find number of records from the database.
     *
     * @return              number of record in database
     */
    @Override
    public long findNumberRecords() {
        return orderDAO.findNumberRecords();
    }

    /**
     * Find number of records from the database by customer.
     *
     * @param customer      customer from request
     * @return              number of record in database
     */
    @Override
    public long findNumberRecordsByCustomer(String customer) {
        return orderDAO.findNumberRecordsByCustomer(customer);
    }

    /**
     * Find number of records from the database by date start order.
     *
     * @param startedAt     trip start date and time
     * @return              number of record in database
     */
    @Override
    public long findNumberRecordsByDateStartedAt(LocalDateTime startedAt) {
        return orderDAO.findNumberRecordsByDateStartedAt(startedAt);
    }

    /**
     * Find sum order`s cost by customer from the database.
     *
     * @param request       request with order`s parameters
     * @return              sum order`s cost by customer
     */
    @Override
    public double sumCostByCustomer(OrderRequest request) {
        return orderDAO.sumCostByCustomer(request);
    }

    /**
     * Update the order from database.
     *
     * @param id            id of order
     * @param request  request with parameter
     */
    @Override
    public void updateById(long id, OrderRequest request) {
        orderDAO.updateById(id, request);
    }

    /**
     * Delete the order from database.
     *
     * @param id            id of category
     */
    @Override
    public void deleteById(long id) {
        orderDAO.deleteById(id);
    }
}
