package com.food.ordering.system.order.service.domain;

import static com.food.ordering.system.domain.DomainConstants.UTC;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.food.ordering.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import com.food.ordering.system.saga.SagaStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderPaymnetSaga implements SagaStep<PaymentResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderSagaHelper orderSagaHelper;
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final OrderRepository orderRepository;
    private final OrderDataMapper orderDataMapper;

    // generate constructor
    public OrderPaymnetSaga(OrderDomainService orderDomainService,
            OrderSagaHelper orderSagaHelper,
            PaymentOutboxHelper paymentOutboxHelper,
            OrderRepository orderRepository,
            ApprovalOutboxHelper approvalOutboxHelper,
            OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderSagaHelper = orderSagaHelper;
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.approvalOutboxHelper = approvalOutboxHelper;
        this.orderRepository = orderRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    @Transactional
    public void process(PaymentResponse paymentResponse) {
        Optional<OrderPaymentOutboxMessage> orderPaymnetOutboxResponse = paymentOutboxHelper
                .getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(paymentResponse.getSagaId()));
        if (orderPaymnetOutboxResponse.isEmpty()) {
            log.info("An outbox message with saga id: {} is already processed", paymentResponse.getSagaId());
            return;
        }

        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymnetOutboxResponse.get();
        OrderPaidEvent domainEvent = completePaymentForOrder(paymentResponse);
        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(domainEvent.getOrder().getOrderStatus());
        paymentOutboxHelper.save(getUpdatePaymentOutboxMessage(orderPaymentOutboxMessage,
                domainEvent.getOrder().getOrderStatus(), sagaStatus));

        approvalOutboxHelper.savaApprovalOutboxHelper(
                orderDataMapper.orderPaidEventToORderApprovalEventPayload(domainEvent),
                domainEvent.getOrder().getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getSagaId()));

        log.info("Order paid for id {}", domainEvent.getOrder().getId().getValue());
    }

    @Override
    @Transactional
    public void rollback(PaymentResponse paymentResponse) {
        Optional<OrderPaymentOutboxMessage> orderPaymnetOutboxResponse = paymentOutboxHelper
                .getPaymentOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(paymentResponse.getSagaId()),
                        getCurrentSagaStatus(paymentResponse.getPaymentStatus()));
        if (orderPaymnetOutboxResponse.isEmpty()) {
            log.error("An outbox message with saga id {} is already rollback", paymentResponse.getSagaId());
            return;
        }
        OrderPaymentOutboxMessage orderPaymentOutboxMessage = orderPaymnetOutboxResponse.get();

        Order order = responseOrderPaymentRollback(paymentResponse);
        SagaStatus sagaStatus = orderSagaHelper.orderStatusToSagaStatus(order.getOrderStatus());
        log.info("Order payment rollback for id {}", order.getId().getValue());
        paymentOutboxHelper.save(getUpdatePaymentOutboxMessage(orderPaymentOutboxMessage,
                order.getOrderStatus(),
                sagaStatus));

        if (paymentResponse.getPaymentStatus() == PaymentStatus.CANCELLED) {
            approvalOutboxHelper.save(getUpdateApprovalOutboxMessage(paymentResponse.getSagaId(),
                    order.getOrderStatus(), sagaStatus));
        }
    }

    private OrderPaymentOutboxMessage getUpdatePaymentOutboxMessage(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
            OrderStatus orderStatus, SagaStatus sagaStatus) {
        orderPaymentOutboxMessage.setProcessedAt(ZonedDateTime.now(ZoneId.of("UTC")));
        orderPaymentOutboxMessage.setStatus(sagaStatus);
        orderPaymentOutboxMessage.setOrderStatus(orderStatus);
        return orderPaymentOutboxMessage;
    }

    private OrderPaidEvent completePaymentForOrder(PaymentResponse paymentResponse) {
        log.info("Compliting order payment for order id: {}", paymentResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent domainEvent = orderDomainService.payOrder(order);
        orderRepository.save(order);
        return domainEvent;
    }

    private SagaStatus[] getCurrentSagaStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case COMPLETED -> new SagaStatus[] { SagaStatus.STARTED };
            case CANCELLED -> new SagaStatus[] { SagaStatus.PROCESSING };
            case FAILED -> new SagaStatus[] { SagaStatus.STARTED, SagaStatus.PROCESSING };
        };

    }

    private Order responseOrderPaymentRollback(PaymentResponse paymentResponse) {
        log.info("Rollback order payment for order id: {}", paymentResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderRepository.save(order);
        return order;
    }

    private OrderApprovalOutboxMessage getUpdateApprovalOutboxMessage(String sagaId,
            OrderStatus orderStatus,
            SagaStatus sagaStatus) {
        Optional<OrderApprovalOutboxMessage> orderApprovalOutboxApprovalResponse = approvalOutboxHelper
                .getApprovalOutboxMessageBySagaIdAndSagaStatus(UUID.fromString(sagaId), SagaStatus.COMPENSATING);
        if (orderApprovalOutboxApprovalResponse.isEmpty()) {
            throw new OrderDomainException(
                    "Approval outbox mesage could not be found in  " + SagaStatus.COMPENSATING.name() + "status");
        }
        OrderApprovalOutboxMessage orderApprovalOutboxMessage = orderApprovalOutboxApprovalResponse.get();
        orderApprovalOutboxMessage.setCreatedAt(ZonedDateTime.now(ZoneId.of(UTC)));
        orderApprovalOutboxMessage.setOrderStatus(orderStatus);
        orderApprovalOutboxMessage.setStatus(sagaStatus);
        return orderApprovalOutboxMessage;

    }
}
