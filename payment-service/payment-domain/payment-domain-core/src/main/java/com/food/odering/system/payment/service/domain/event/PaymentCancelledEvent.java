package com.food.odering.system.payment.service.domain.event;

import com.food.odering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCancelledEvent extends PaymentEvent{

    private final DomainEventPublisher<PaymentCancelledEvent> paymentCencellDomainEventPublisher;

    public PaymentCancelledEvent(Payment payment,
                             ZonedDateTime createdAt,
                              DomainEventPublisher<PaymentCancelledEvent> paymentCencellDomainEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.paymentCencellDomainEventPublisher=paymentCencellDomainEventPublisher;

    }

    @Override
    public void fire() {
        paymentCencellDomainEventPublisher.publish(this);
   }
}
