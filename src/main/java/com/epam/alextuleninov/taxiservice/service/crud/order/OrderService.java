package com.epam.alextuleninov.taxiservice.service.crud.order;

import com.epam.alextuleninov.taxiservice.dao.order.OrderDAO;
import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;
import com.epam.alextuleninov.taxiservice.data.order.OrderRequest;
import com.epam.alextuleninov.taxiservice.data.pageable.PageableRequest;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

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
    public Order create(OrderRequest request) {
        return orderDAO.create(request);
    }

    /**
     * Find all orders from the database with pagination information.
     *
     * @param pageable      pageable with pagination information
     * @return              all orders from database with pagination information in response format
     */
    @Override
    public List<OrderResponse> findAll(PageableRequest pageable, String locale) {
        return orderDAO.findAll(pageable).stream()
                .map(order -> OrderResponse.fromOrder(order, locale))
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
     * @param request       request with order`s parameters
     * @param pageable      request with pagination information
     * @return              all users by range from database in response format
     */
    @Override
    public List<OrderResponse> findAllByCustomer(OrderRequest request, PageableRequest pageable, String locale) {
        return orderDAO.findAllByCustomer(request, pageable)
                .stream()
                .map(order -> OrderResponse.fromOrder(order, locale))
                .toList();
    }

    /**
     * Find all orders by date start order from the database.
     *
     * @param request       request with order`s parameters
     * @param pageable      request with pagination information
     * @return              all users by range from database in response format
     */
    @Override
    public List<OrderResponse> findAllByDate(OrderRequest request, PageableRequest pageable, String locale) {
        return orderDAO.findAllByDate(request, pageable)
                .stream()
                .map(order -> OrderResponse.fromOrder(order, locale))
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
        listStartedAtDates.addAll(0, Arrays.asList("--------------------------", "all orders"));

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
     * @param request       request with order`s parameters
     * @return              number of record in database
     */
    @Override
    public long findNumberRecordsByCustomer(OrderRequest request) {
        return orderDAO.findNumberRecordsByCustomer(request);
    }

    /**
     * Find number of records from the database by date start order.
     *
     * @param request       request with order`s parameters
     * @return              number of record in database
     */
    @Override
    public long findNumberRecordsByDateStartedAt(OrderRequest request) {
        return orderDAO.findNumberRecordsByDateStartedAt(request);
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
}
