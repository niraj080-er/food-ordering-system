package com.food.odering.system.payment.service.domain.event;

import com.food.odering.system.payment.service.domain.entity.Payment;
import com.food.ordering.system.domain.event.DomainEvent;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class PaymentEvent  implements DomainEvent<Payment> {

    private final  Payment payment;
    private final ZonedDateTime createdAt;
    private  final List<String> failureMessage;

    public PaymentEvent(Payment payment, ZonedDateTime createdAt, List<String> failuerMessage) {
        this.payment = payment;
        this.createdAt = createdAt;
        this.failureMessage = failuerMessage;
    }

    public Payment getPayment() {
        return payment;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public List<String> getFailureMessage() {
        return failureMessage;
    }
}
