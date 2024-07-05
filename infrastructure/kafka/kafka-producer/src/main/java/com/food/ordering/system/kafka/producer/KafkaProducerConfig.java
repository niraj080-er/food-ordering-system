package com.food.ordering.system.kafka.producer;

import java.io.Serializable;

import org.apache.avro.specific.SpecificRecordBase;

import com.food.odering.system.kafka.config.data.KafkaConfigData;
import com.food.odering.system.kafka.config.data.KafkaProducerConfigData;

public class KafkaProducerConfig <K extends Serializable, V extends SpecificRecordBase>{

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData kafkaProducerConfigData;
    public KafkaProducerConfig(KafkaConfigData kafkaConfigData, KafkaProducerConfigData kafkaProducerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducerConfigData = kafkaProducerConfigData;
    }


    
}
