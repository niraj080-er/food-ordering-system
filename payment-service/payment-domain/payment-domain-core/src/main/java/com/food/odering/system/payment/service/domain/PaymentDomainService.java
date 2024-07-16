package com.food.odering.system.payment.service.domain;

import com.food.odering.system.payment.service.domain.entity.CreditEntity;
import com.food.odering.system.payment.service.domain.entity.CreditHistory;
import com.food.odering.system.payment.service.domain.entity.Payment;
import com.food.odering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.odering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.odering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;

import java.util.List;

public interface PaymentDomainService  {

    PaymentEvent validateInitiatePayment(Payment payment,
                                         CreditEntity creditEntity,
                                         List<CreditHistory> creditHistories,
                                         List<String> failureMessage, DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                          CreditEntity creditEntity,
                                          List<CreditHistory> creditHistories,
                                          List<String> failureMessage, DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher);
}
