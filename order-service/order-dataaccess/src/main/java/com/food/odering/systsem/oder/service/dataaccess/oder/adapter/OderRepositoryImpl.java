package com.food.odering.systsem.oder.service.dataaccess.oder.adapter;

import com.food.odering.systsem.oder.service.dataaccess.oder.mapper.OrderDataAccessMapper;
import com.food.odering.systsem.oder.service.dataaccess.oder.repository.OrderJpaRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.valueobject.TrackingId;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class OderRepositoryImpl implements OrderRepository
{
    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;
    public OderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }
    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJpaRepository
                .save(orderDataAccessMapper.orderToOrderEntity(order)));
    }
    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingID(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}