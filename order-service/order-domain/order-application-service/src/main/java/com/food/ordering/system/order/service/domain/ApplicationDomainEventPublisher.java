package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.service.domain.events.OrderCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationDomainEventPublisher implements DomainEventPublisher<OrderCreateEvent> ,
        ApplicationEventPublisher {

    private ApplicationEventPublisher applicationEventPublisher;

    public ApplicationDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishEvent(@SuppressWarnings("null") Object event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(OrderCreateEvent domainEvent) {
        log.info("publishing event");
        applicationEventPublisher.publishEvent(domainEvent);
    }
    

   
}
