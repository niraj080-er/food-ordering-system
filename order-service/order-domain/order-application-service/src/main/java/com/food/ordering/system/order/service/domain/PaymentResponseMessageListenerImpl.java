package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import static com.food.ordering.system.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Slf4j
@Validated
@Service
public class PaymentResponseMessageListenerImpl implements PaymentResponseMessageListener {

    private final OrderPaymnetSaga orderPaymnetSaga;
    // generate constructor
    public PaymentResponseMessageListenerImpl(OrderPaymnetSaga orderPaymnetSaga) {
        this.orderPaymnetSaga = orderPaymnetSaga;
    }
    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
       OrderPaidEvent orderPaidEvent= orderPaymnetSaga.process(paymentResponse);
       log.info("Order paid event published for order id: {}", paymentResponse.getOrderId());
       orderPaidEvent.fire();

    }

    @Override
    public void paymentCancelled(PaymentResponse paymentResponse) {
        orderPaymnetSaga.rollback(paymentResponse);
        log.info("Order payment rollback for order id: {}",  String.join(FAILURE_MESSAGE_DELIMITER,paymentResponse.getFailureMessages()));

    }
}
