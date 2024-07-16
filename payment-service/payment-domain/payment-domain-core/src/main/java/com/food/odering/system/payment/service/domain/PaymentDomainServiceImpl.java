package com.food.odering.system.payment.service.domain;

import com.food.odering.system.payment.service.domain.entity.CreditEntity;
import com.food.odering.system.payment.service.domain.entity.CreditHistory;
import com.food.odering.system.payment.service.domain.entity.Payment;
import com.food.odering.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.odering.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.odering.system.payment.service.domain.event.PaymentEvent;
import com.food.odering.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.odering.system.payment.service.domain.valuobject.CreditHistoryId;
import com.food.odering.system.payment.service.domain.valuobject.TransactionType;
import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.food.ordering.system.domain.DomainConstants.UTC;

@Slf4j

public class PaymentDomainServiceImpl implements PaymentDomainService{
    /**
     * @param payment
     * @param creditEntity
     * @param creditHistories
     * @param failureMessage
     * @param paymentCompletedEventDomainEventPublisher
     * @return
     */
    @Override
    public PaymentEvent validateInitiatePayment(Payment payment,
                                                CreditEntity creditEntity,
                                                List<CreditHistory> creditHistories,
                                                List<String> failureMessage,
                                                DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher) {
        payment.validatePayment(failureMessage);
        payment.initializePayment();
        validateCreditEntry(payment, creditEntity,failureMessage);
        subtractCreditEntry(payment, creditEntity);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntity, creditHistories, failureMessage);
        if (failureMessage.isEmpty()) {
            log.info("Payment is initialized for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.COMPLETED);
            return new PaymentCompletedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)),paymentCompletedEventDomainEventPublisher);
        } else {
            log.info("Payment initialization failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessage);
        }
    }

    private void validateCreditHistory(CreditEntity creditEntity,
                                       List<CreditHistory> creditHistories,
                                       List<String> failureMessage) {
        Money totalCreditHistory=geTotalHistoryAmount(creditHistories,TransactionType.CREDIT);
        Money totalDebitHistory=geTotalHistoryAmount(creditHistories,TransactionType.DEBIT);
        if(totalDebitHistory.isGreaterThan(totalCreditHistory)){
            log.error("Customer with id :{} does not have enough credit according to the credit history",creditEntity.getCustomerId().getValue());
            failureMessage.add("Customer with id = " + creditEntity.getCustomerId().getValue() + " does not have enough credit according to credit history");
        }
        if(!creditEntity.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalCreditHistory))){
            log.error("Credit history total is not equal to current credit for customer Id: {} ", creditEntity.getCustomerId()
                    .getValue());
            failureMessage.add("Credit history total is not equal to current credit for customer id: "+ creditEntity.getCustomerId().getValue() + "!");
        }
    }

    private Money geTotalHistoryAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        return creditHistories.stream()
                .filter(creditHistory -> transactionType==creditHistory.getTransactionType())
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);
    }

    private void updateCreditHistory(Payment payment,
                                     List<CreditHistory> creditHistories,
                                     TransactionType transactionType) {
        creditHistories.add(CreditHistory.builder()
                .creditHistoryId(new CreditHistoryId(UUID.randomUUID()))
                .customerId(payment.getCustomerId())
                .amount(payment.getPrice())
                .transactionType(transactionType)
                .build());
    }


    private void subtractCreditEntry(Payment payment, CreditEntity creditEntity) {
        creditEntity.addCreditAmount(payment.getPrice());
    }

    private void validateCreditEntry(Payment payment, CreditEntity creditEntity, List<String> failureMessage) {
        if (payment.getPrice().isGreaterThan(creditEntity.getTotalCreditAmount())) ;
        {
            log.error("Customer  does not have enough credit for payment", payment.getCustomerId().getValue());
            failureMessage.add("Customer with id = " + payment.getCustomerId().getValue() + " does not have enough for payment !");
        }

    }
    /**
     * @param payment
     * @param creditEntity
     * @param creditHistories
     * @param failureMessage
     * @param paymentCancelledEventDomainEventPublisher
     * @return
     */
    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment,
                                                 CreditEntity creditEntity,
                                                 List<CreditHistory> creditHistories,
                                                 List<String> failureMessage,
                                                 DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher) {
        payment.validatePayment(failureMessage);
        addCreditEntry(payment, creditEntity);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);
        if (failureMessage.isEmpty()) {
            log.info("Payment is cancelled for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.CANCELLED);
            return new PaymentCancelledEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)),paymentCancelledEventDomainEventPublisher);
        } else {
            log.info("Payment cancellation is failed for order id: {}", payment.getOrderId().getValue());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, ZonedDateTime.now(ZoneId.of(UTC)), failureMessage);
        }
    }
    private void addCreditEntry(Payment payment, CreditEntity creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }
}
