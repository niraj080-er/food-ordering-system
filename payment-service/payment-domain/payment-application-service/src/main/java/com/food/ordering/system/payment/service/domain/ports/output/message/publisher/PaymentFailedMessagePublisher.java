package com.food.ordering.system.payment.service.domain.ports.output.message.publisher;

import com.food.odering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent> {
}
