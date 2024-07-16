package com.food.odering.system.payment.service.domain.event;

import com.food.odering.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent{
    @Override
    public void fire() {

    }

    public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failuerMessage) {
        super(payment, createdAt, failuerMessage);
    }
}
