package com.food.ordering.system.restaurant.service.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public  abstract class OrderApprovalEvent implements DomainEvent {

    private  final OrderApproval orderApproval;
    private final RestaurantId restaurantId;
    private  final List<String> failureMessages;
    private final ZonedDateTime createdAt;


    }
