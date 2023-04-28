package com.epam.alextuleninov.taxiservice.service.crud.order;

import com.epam.alextuleninov.taxiservice.dao.OrderDAO;
import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;
import com.epam.alextuleninov.taxiservice.model.order.Order;
import com.epam.alextuleninov.taxiservice.service.crud.OrderCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_SORT_ALL;
import static com.epam.alextuleninov.taxiservice.Constants.SCOPE_SORT_NOTHING;
import static com.epam.alextuleninov.taxiservice.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderDAO orderDAO;
    private OrderCRUD orderCRUD;

    @BeforeEach
    void setUp() {
        this.orderDAO = mock(OrderDAO.class);
        this.orderCRUD = new OrderService(orderDAO);
    }

    @Test
    void testCreate() {
        when(orderDAO.create(getTestOrderRequest())).thenReturn(getTestOrder());
        var orderResponse = orderCRUD.create(getTestOrderRequest());

        assertEquals(OrderResponse.fromOrder(getTestOrder()), orderResponse);
    }

    @Test
    void testFindAll() {
        List<Order> orders = new ArrayList<>();
        orders.add(getTestOrder());

        when(orderDAO.findAll(getTestPageableRequest())).thenReturn(orders);
        var resultPresent = orderCRUD.findAll(getTestPageableRequest());

        assertNotNull(resultPresent);
        assertEquals(1, resultPresent.size());
        assertEquals(OrderResponse.fromOrder(getTestOrder()), resultPresent.get(0));

        when(orderDAO.findAll(getTestPageableRequest())).thenReturn(new ArrayList<>());
        var resultAbsent = orderCRUD.findAll(getTestPageableRequest());

        assertEquals(0, resultAbsent.size());
    }

    @Test
    void testFindAllByRange() {
        List<Order> orders = new ArrayList<>();
        orders.add(getTestOrder());

        when(orderDAO.findAllByRange(getTestOrderRequest())).thenReturn(orders);
        var resultPresent = orderCRUD.findAllByRange(getTestOrderRequest());

        assertNotNull(resultPresent);
        assertEquals(1, resultPresent.size());
        assertEquals(getTestOrder(), resultPresent.get(0));

        when(orderDAO.findAllByRange(getTestOrderRequest())).thenReturn(new ArrayList<>());
        var resultAbsent = orderCRUD.findAllByRange(getTestOrderRequest());

        assertEquals(0, resultAbsent.size());
    }

    @Test
    void testFindAllByCustomer() {
        List<Order> orders = new ArrayList<>();
        orders.add(getTestOrder());

        when(orderDAO.findAllByCustomer(getTestOrder().getCustomer().getEmail(), getTestPageableRequest())).thenReturn(orders);
        var resultPresent = orderCRUD.findAllByCustomer(getTestOrder().getCustomer().getEmail(), getTestPageableRequest());

        assertNotNull(resultPresent);
        assertEquals(1, resultPresent.size());
        assertEquals(OrderResponse.fromOrder(getTestOrder()), resultPresent.get(0));

        when(orderDAO.findAllByCustomer(getTestOrder().getCustomer().getEmail(), getTestPageableRequest())).thenReturn(new ArrayList<>());
        var resultAbsent = orderCRUD.findAllByCustomer(getTestOrder().getCustomer().getEmail(), getTestPageableRequest());

        assertEquals(0, resultAbsent.size());
    }

    @Test
    void testFindAllByDate() {
        List<Order> orders = new ArrayList<>();
        orders.add(getTestOrder());

        when(orderDAO.findAllByDate(getTestOrder().getStartedAt(), getTestPageableRequest())).thenReturn(orders);
        var resultPresent = orderCRUD.findAllByDate(getTestOrder().getStartedAt(), getTestPageableRequest());

        assertNotNull(resultPresent);
        assertEquals(1, resultPresent.size());
        assertEquals(OrderResponse.fromOrder(getTestOrder()), resultPresent.get(0));

        when(orderDAO.findAllByDate(getTestOrder().getStartedAt(), getTestPageableRequest())).thenReturn(new ArrayList<>());
        var resultAbsent = orderCRUD.findAllByDate(getTestOrder().getStartedAt(), getTestPageableRequest());

        assertEquals(0, resultAbsent.size());
    }

    @Test
    void testFindByID() {
        // if value is present
        when(orderDAO.findByID(0)).thenReturn(Optional.of(getTestOrder()));
        Optional<OrderResponse> resultPresent = orderCRUD.findByID(0);

        assertNotNull(resultPresent);
        assertEquals(Optional.of(OrderResponse.fromOrder(getTestOrder())), resultPresent);

        // if value is absent
        when(orderDAO.findByID(0)).thenReturn(Optional.empty());
        Optional<OrderResponse> resultAbsent = orderCRUD.findByID(0);

        assertEquals(Optional.empty(), resultAbsent);
    }

    @Test
    void testFindAllStartedAtDatesFromOrder() {
        LocalDateTime date = LocalDateTime.parse("2007-12-03T10:15:30");
        Set<LocalDateTime> dates = new TreeSet<>();
        dates.add(date);

        when(orderDAO.findAllStartedAtDatesFromOrder()).thenReturn(dates);
        var resultPresent = orderCRUD.findAllStartedAtDatesFromOrder();

        assertNotNull(resultPresent);
        assertEquals(3, resultPresent.size());
        assertEquals(SCOPE_SORT_NOTHING, resultPresent.get(0));
        assertEquals(SCOPE_SORT_ALL, resultPresent.get(1));
        assertEquals(date.toLocalDate().toString(), resultPresent.get(2));

        when(orderDAO.findAllStartedAtDatesFromOrder()).thenReturn(new TreeSet<>());
        var resultAbsent = orderCRUD.findAllStartedAtDatesFromOrder();

        assertNotNull(resultAbsent);
        assertEquals(2, resultAbsent.size());
        assertEquals(SCOPE_SORT_NOTHING, resultAbsent.get(0));
        assertEquals(SCOPE_SORT_ALL, resultAbsent.get(1));
    }

    @Test
    void testFindNumberRecords() {
        // if value is present
        when(orderDAO.findNumberRecords()).thenReturn(1L);
        long resultPresent = orderCRUD.findNumberRecords();

        assertEquals(1, resultPresent);

        // if value is absent
        when(orderDAO.findNumberRecords()).thenReturn(0L);
        long resultAbsent = orderCRUD.findNumberRecords();

        assertEquals(0, resultAbsent);
    }

    @Test
    void testFindNumberRecordsByCustomer() {
        // if value is present
        when(orderDAO.findNumberRecordsByCustomer(getTestUser().getEmail())).thenReturn(1L);
        long resultPresent = orderCRUD.findNumberRecordsByCustomer(getTestUser().getEmail());

        assertEquals(1, resultPresent);

        // if value is absent
        when(orderDAO.findNumberRecordsByCustomer(getTestUser().getEmail())).thenReturn(0L);
        long resultAbsent = orderCRUD.findNumberRecordsByCustomer(getTestUser().getEmail());

        assertEquals(0, resultAbsent);
    }

    @Test
    void testFindNumberRecordsByDateStartedAt() {
        // if value is present
        when(orderDAO.findNumberRecordsByDateStartedAt(getTestOrderRequest().startedAt())).thenReturn(1L);
        long resultPresent = orderCRUD.findNumberRecordsByDateStartedAt(getTestOrderRequest().startedAt());

        assertEquals(1, resultPresent);

        // if value is absent
        when(orderDAO.findNumberRecordsByDateStartedAt(getTestOrderRequest().startedAt())).thenReturn(0L);
        long resultAbsent = orderCRUD.findNumberRecordsByDateStartedAt(getTestOrderRequest().startedAt());

        assertEquals(0, resultAbsent);
    }

    @Test
    void testSumCostByCustomer() {
        // if value is present
        double sum = 40.0;
        when(orderDAO.sumCostByCustomer(getTestOrderRequest())).thenReturn(sum);
        double resultPresent = orderCRUD.sumCostByCustomer(getTestOrderRequest());

        assertEquals(sum, resultPresent);

        // if value is absent
        double sumO = 0.0;
        when(orderDAO.sumCostByCustomer(getTestOrderRequest())).thenReturn(sumO);
        double resultAbsent = orderCRUD.sumCostByCustomer(getTestOrderRequest());

        assertEquals(sumO, resultAbsent);
    }

    @Test
    void testUpdateByID() {
        doNothing().when(orderDAO).updateByID(0L, getTestOrderRequest());
        assertDoesNotThrow(() -> orderCRUD.updateByID(0L, getTestOrderRequest()));
    }

    @Test
    void testDeleteByID() {
        doNothing().when(orderDAO).deleteByID(0L);
        assertDoesNotThrow(() -> orderCRUD.deleteByID(0L));
    }
}
