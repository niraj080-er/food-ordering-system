package com.food.ordering.system.kafka.producer.services;

import java.io.Serializable;

import org.apache.avro.specific.SpecificRecordBase;
import org.checkerframework.checker.units.qual.K;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

public interface KafkaProducer <K extends Serializable, V extends SpecificRecordBase>
{
    void send(String topicName, K key,  V message, ListenableFutureCallback<SendResult<K,V>> callback);


}
