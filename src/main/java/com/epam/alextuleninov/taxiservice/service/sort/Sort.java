package com.epam.alextuleninov.taxiservice.service.sort;

import com.epam.alextuleninov.taxiservice.data.order.OrderResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.alextuleninov.taxiservice.Constants.SORTING_ASC;

public class Sort implements Sortable {

    @Override
    public List<OrderResponse> sorting(Object sortTypeByDateFromSession,
                                       Object sortTypeByCostFromSession,
                                       List<OrderResponse> allOrders) {
        // sorting by date
        if (sortTypeByDateFromSession != null) {
            if (sortTypeByDateFromSession.equals(SORTING_ASC)) {
                allOrders = allOrders.stream()
                        .sorted(Comparator.comparing(OrderResponse::getCreatedAt))
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                allOrders = allOrders.stream()
                        .sorted(Comparator.comparing(OrderResponse::getCreatedAt).reversed())
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }

        // sorting by cost
        if (sortTypeByCostFromSession != null) {
            if (sortTypeByCostFromSession.equals(SORTING_ASC)) {
                allOrders = allOrders.stream()
                        .sorted(Comparator.comparingDouble(OrderResponse::getCost))
                        .collect(Collectors.toCollection(ArrayList::new));
            } else {
                allOrders = allOrders.stream()
                        .sorted(Comparator.comparingDouble(OrderResponse::getCost).reversed())
                        .collect(Collectors.toCollection(ArrayList::new));
            }
        }
        return allOrders;
    }
}
