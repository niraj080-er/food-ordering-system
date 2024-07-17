package com.food.odering.system.payment.service.domain.event;

import com.food.odering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent{

    private final DomainEventPublisher<PaymentFailedEvent> paymentFailedventDomainEventPublisher;

    public PaymentFailedEvent(Payment payment,
                            ZonedDateTime createdAt,
                            List<String> failuerMessage, 
                            DomainEventPublisher<PaymentFailedEvent> paymentFailedventDomainEventPublisher) {
        super(payment, createdAt, failuerMessage);
        this.paymentFailedventDomainEventPublisher = paymentFailedventDomainEventPublisher;
    }

    @Override
    public void fire() {
        paymentFailedventDomainEventPublisher.publish(this);
    }

    
}
