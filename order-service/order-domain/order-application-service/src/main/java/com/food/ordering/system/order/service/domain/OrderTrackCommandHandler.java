package com.food.ordering.system.order.service.domain;


import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Transactional;
import com.food.ordering.system.service.domain.exception.OrderNotFoundException;
import com.food.ordering.system.service.domain.valueobject.TrackingId;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    private OrderDataMapper orderDataMapper;
    private OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderDataMapper orderDataMapper, OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
       Optional<Order> orderResult= orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
            if(orderResult.isEmpty()){
                log.warn("could not found the order with the tracking id {}",trackOrderQuery.getOrderTrackingId());
                throw new OrderNotFoundException("could not found the order with the tracking id "+trackOrderQuery.getOrderTrackingId());
            }
            return orderDataMapper.orderToTrackOrderResponse(orderResult.get(), null) ;
    }

}
