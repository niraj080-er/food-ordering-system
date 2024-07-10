package com.food.odering.system.service.messaging.publisher.kafka;


import com.food.odering.system.service.messaging.mapper.OderMessagingDataMapper;
import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.kafka.producer.services.KafkaProducer;
import com.food.ordering.system.order.service.domain.config.OderServiceConfigData;
import com.food.ordering.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPaymentRequestMesagePublisher;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.support.converter.KafkaMessageHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMesagePublisher {

    private final OderMessagingDataMapper orderMessagingDataMapper;
    private final OderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;
    private final KafkaMessageHeaders orderKafkaMessageHelper;

    public CreateOrderKafkaMessagePublisher(KafkaMessageHeaders orderKafkaMessageHelper,
                                            OderMessagingDataMapper orderMessagingDataMapper,
                                            OderServiceConfigData orderServiceConfigData,
                                            KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer) {
        this.orderKafkaMessageHelper = orderKafkaMessageHelper;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void publish(DomainEvent domainEvent) {


    }
}
