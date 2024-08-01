package com.food.ordering.system.order.service.domain.outbox.scheduler.approval;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.food.ordering.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordering.system.outbox.OutboxScheduler;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RestaurantApprovalOutboxSchedulerCleaner  implements OutboxScheduler{

    private final ApprovalOutboxHelper approvalOutboxHelper;
    public RestaurantApprovalOutboxSchedulerCleaner(ApprovalOutboxHelper approvalOutboxHelper) {
        this.approvalOutboxHelper = approvalOutboxHelper;
    }

    @Override
    @Scheduled(cron="@midnight")
    public void processOutboxMessage() {
        Optional<List<OrderApprovalOutboxMessage>> outboxMessageApproval= approvalOutboxHelper.getApprovalOutboxMessageByOutboxStatusAndSagaStatus(
            OutboxStatus.COMPLETED, 
            SagaStatus.SUCED,
            SagaStatus.FAILED,
            SagaStatus.COMPENSATING );
        if(outboxMessageApproval.isPresent()){
            List<OrderApprovalOutboxMessage> outboxMessages=outboxMessageApproval.get();
            log.info("Received {} OrderApprovalOutboxMessage for clean-up, The Playloads : {} ",
               outboxMessages.size(),
                    outboxMessages.stream().map(OrderApprovalOutboxMessage::getPayload).collect(Collectors.joining("\n")));

        approvalOutboxHelper.deleteApprovalOutboxmessageByOutboxStatusAndSagaStus( OutboxStatus.COMPLETED, 
        SagaStatus.SUCED,
        SagaStatus.FAILED,
        SagaStatus.COMPENSATING );
        log.info("{} OrderApprovalOutboxMessage cleaned up successfully !",
                outboxMessages.size());
        }

    }
}
