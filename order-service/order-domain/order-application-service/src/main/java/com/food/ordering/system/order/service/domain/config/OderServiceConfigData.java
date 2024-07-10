package com.food.ordering.system.order.service.domain.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "oder-service")
public class OderServiceConfigData {
    private  String paymentRequestTopicName;
    private  String paymentResponseTopicName;
    private  String restaurantApprovalRequestTopicName;
    private  String restaurantApprovalResponseTopicName;

}
