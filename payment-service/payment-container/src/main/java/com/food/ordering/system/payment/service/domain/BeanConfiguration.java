package com.food.ordering.system.payment.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.food.odering.system.payment.service.domain.PaymentDomainService;
import com.food.odering.system.payment.service.domain.PaymentDomainServiceImpl;

@Configuration
public class BeanConfiguration {

    @Bean
    public PaymentDomainService paymentDomainService(){
        return new PaymentDomainServiceImpl();

    }
    
}
