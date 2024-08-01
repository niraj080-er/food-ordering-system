package com.food.ordering.system.order.service.domain.outbox.scheduler.payment;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentOutboxSchedulerCleaner  implements OutboxScheduler{
    private final PaymentOutboxHelper paymentHelper;

    // generate constructor parameters
    public PaymentOutboxSchedulerCleaner(PaymentOutboxHelper paymentHelper) {
        this.paymentHelper = paymentHelper;
    }

    @Scheduled(cron="@midnight")
    @Override
    public void processOutboxMessage() {
       Optional< List<OrderPaymentOutboxMessage>> outboxMessagesResponse = paymentHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus.COMPLETED, 
            SagaStatus.SUCED,
            SagaStatus.FAILED,
            SagaStatus.COMPENSATING );
        if(outboxMessagesResponse.isPresent()){
            List<OrderPaymentOutboxMessage> outboxMessages=outboxMessagesResponse.get();
            log.info("Received {} OrderPaymentOutboxMessage for clean-up, The Playloads : {} ",
               outboxMessages.size(),
                    outboxMessages.stream().map(OrderPaymentOutboxMessage::getPayload).collect(Collectors.joining("\n")));
        
            paymentHelper.deletePaymentOutboxMessageByOrderStatusAndSagaStatus(OutboxStatus.COMPLETED, 
                                                                            SagaStatus.SUCED, 
                                                                            SagaStatus.FAILED, 
                                                                            SagaStatus.COMPENSATING);
            log.info("{} OrderPaymentOutboxMessage cleaned up successfully !",
                    outboxMessages.size());
        }
            
        
    }



}
