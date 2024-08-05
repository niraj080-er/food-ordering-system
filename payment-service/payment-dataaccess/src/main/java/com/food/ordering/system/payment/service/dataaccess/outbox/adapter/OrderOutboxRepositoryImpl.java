package com.food.ordering.system.payment.service.dataaccess.outbox.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.dataaccess.outbox.exception.OrderOutboxNotFoundException;
import com.food.ordering.system.payment.service.dataaccess.outbox.mapper.OrderOutboxDataAcessMapper;
import com.food.ordering.system.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.ports.output.repository.OrderOutboxRepository;

@Component
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

    private final OrderOutboxJpaRepository orderOutboxJPaRepository;
    private OrderOutboxDataAcessMapper orderOutboxDataAcessMapper;

    // genrated constructor
    public OrderOutboxRepositoryImpl(OrderOutboxJpaRepository orderOutboxJPaRepository,
            OrderOutboxDataAcessMapper orderOutboxDataAcessMapper) {
        this.orderOutboxJPaRepository = orderOutboxJPaRepository;
        this.orderOutboxDataAcessMapper = orderOutboxDataAcessMapper;
    }

    @Override
    public OrderOutboxMessage save(OrderOutboxMessage orderPaymentOutboxMessage) {
        return orderOutboxDataAcessMapper
                .orderOutboxEntityToOrderOutboxMessage(orderOutboxJPaRepository
                        .save(orderOutboxDataAcessMapper
                                .orderOutboxMessageToOutboxEntity(orderPaymentOutboxMessage)));
    }

    @Override
    public Optional<List<OrderOutboxMessage>> findByTypeAndOutboxStatus(String sagaType, OutboxStatus outboxStatus) {
        return Optional.of(orderOutboxJPaRepository.findByTypeAndOutboxStatus(sagaType, outboxStatus)
                .orElseThrow(() -> new OrderOutboxNotFoundException("Approval outbox object " +
                        "cannot be found for saga type " + sagaType))
                .stream().map(orderOutboxDataAcessMapper::orderOutboxEntityToOrderOutboxMessage)
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<OrderOutboxMessage> findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(String sagaType,
            UUID sagaId,
            PaymentStatus paymentStatus,
            OutboxStatus outboxStatus) {
        return orderOutboxJPaRepository
                .findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(sagaType, sagaId, paymentStatus, outboxStatus)
                .map(orderOutboxDataAcessMapper::orderOutboxEntityToOrderOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatus(String sagaType, OutboxStatus outboxStatus) {
        orderOutboxJPaRepository.deleteByTypeAndOutboxStatus(sagaType, outboxStatus);
    }
}
