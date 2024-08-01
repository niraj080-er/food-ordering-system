package com.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval.RestaurantApprovalRequestMessagePublisher;
import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestaurantApprovalOutboxScheduler implements OutboxScheduler{
    private final ApprovalOutboxHelper approvalOutboxHelper;
    private final RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher ;

    // create constructor
     public RestaurantApprovalOutboxScheduler(ApprovalOutboxHelper approvalOutboxHelper,
                                             RestaurantApprovalRequestMessagePublisher restaurantApprovalRequestMessagePublisher) {
        this.approvalOutboxHelper = approvalOutboxHelper;
        this.restaurantApprovalRequestMessagePublisher = restaurantApprovalRequestMessagePublisher;
    }

    @Transactional
    @Scheduled(fixedDelayString = "${order-service.outbox.payment.fixed-delay}",
            initialDelayString = "${order-service.outbox.payment.initial-delay}")
    @Override    
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> outboxMessageResponse =
                approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
                    OutboxStatus.STARTED, 
                    SagaStatus.PROCESSING);
        if(outboxMessageResponse.isPresent() && outboxMessageResponse.get().size()>0){
            List<OrderApprovalOutboxMessage> outboxMessages = outboxMessageResponse.get();
            log.info("Received {} OrderApprovalOutboxMessage with id:{}, sending  to message bus !",
               outboxMessages.size(),
                    outboxMessages.stream().map(outboxMessage->
                            outboxMessage.getId().toString()).collect(Collectors.joining(","))
            );
            outboxMessages.forEach(outboxMesage -> 
            restaurantApprovalRequestMessagePublisher.publish(outboxMesage,this::updateOutbox));
            log.info("{} RestaurantApprovalOutboxMessage sent to message bus !",
                    outboxMessages.size());
        }
    }
    private void updateOutbox(OrderApprovalOutboxMessage outboxMessage, OutboxStatus outboxStatus){
        approvalOutboxHelper.save(outboxMessage);
        log.info("OrderApprovalOutboxMessage with id:{} updated with status:{}",outboxStatus.name());
    }

}
