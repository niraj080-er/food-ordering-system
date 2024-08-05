package com.food.ordering.system.payment.service.domain.outbox.scheduler;

import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OrderoutboxCleanerScheduler implements OutboxScheduler {

    private final OrderOutboxHelper orderOutboxHelper;

    // generate constructor
    public OrderoutboxCleanerScheduler(OrderOutboxHelper orderOutboxHelper) {
        this.orderOutboxHelper = orderOutboxHelper;
    }

    @Override
    @Scheduled(cron = "@midnight")
    @Transactional
    public void processOutboxMessage() {
        Optional<List<OrderOutboxMessage>>  outboxMessageResponse= orderOutboxHelper
                .getOrderOutboxMessgeByOutboxStatus(OutboxStatus.COMPLETED); 
        if(outboxMessageResponse.isPresent() && outboxMessageResponse.get().size()>0){
        List<OrderOutboxMessage> outboxMessages= outboxMessageResponse.get();
        log.info("Recived {} OrderOutboxMessage for clean-up! ", outboxMessages.size());
        orderOutboxHelper.deleteOrderOutboxMessageByOutboxStatus(OutboxStatus.COMPLETED);
        log.info("Deleted OrderOutboxMessage ", outboxMessages.size());
        }
    }

}
