package com.food.ordering.system.order.service.domain.outbox.scheduler.payment;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.ordering.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestMessagePublisher;
import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

@Slf4j
@Component
public class PaymentOutboxScheduler implements OutboxScheduler {
    private final PaymentOutboxHelper paymentOutboxHelper;
    private final PaymentRequestMessagePublisher paymentRequestMessagePublisher;
    
    public PaymentOutboxScheduler(PaymentOutboxHelper paymentOutboxHelper, PaymentRequestMessagePublisher paymentRequestMessagePublisher) {
        this.paymentOutboxHelper = paymentOutboxHelper;
        this.paymentRequestMessagePublisher = paymentRequestMessagePublisher;
    }
     
    @Transactional
    @Override
    @Scheduled(fixedDelayString = "${order-service.outbox.payment.fixed-delay}",
            initialDelayString = "${order-service.outbox.payment.initial-delay}")
    public void processOutboxMessage() {
        Optional<List<OrderPaymentOutboxMessage>> outboxMessageResponse =
                paymentOutboxHelper.getPaymentOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.STARTED, 
                    SagaStatus.STARTED,
                     SagaStatus.COMPENSATING);
        if(outboxMessageResponse.isPresent() && outboxMessageResponse.get().size()>0){
            List<OrderPaymentOutboxMessage> outboxMessages = outboxMessageResponse.get();
            log.info("Received {} OrderPaymentOutboxMessage with id:{}, sending  to message bus !",
               outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage->
                            outboxMessage.getId().toString()).collect(Collectors.joining(","))
            );
            outboxMessages.forEach(outboxMesage -> 
            paymentRequestMessagePublisher.publish(outboxMesage,this::updateOutboxStatus ));
            log.info("{} OrderPaymentOutboxMessage sent to message bus !",
                    outboxMessages.size());
        }
        
    }
    private void updateOutboxStatus(OrderPaymentOutboxMessage orderPaymentOutboxMessage, 
                                    OutboxStatus outboxStatus){
            orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
            paymentOutboxHelper.save(orderPaymentOutboxMessage);
            log.info("OrderPaymentOutboxMessage with id:{} updated with status:{}",outboxStatus.name());

        
    }


}
