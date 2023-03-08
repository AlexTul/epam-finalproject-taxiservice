package com.epam.alextuleninov.taxiservice.service.sort;

import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;

import java.util.List;

public interface Sortable {

    List<OrderResponse> sorting(Object sortTypeByDateFromSession,
                 Object sortTypeByCostFromSession,
                 List<OrderResponse> allOrders);
}
