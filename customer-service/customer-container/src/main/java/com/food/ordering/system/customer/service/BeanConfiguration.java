package com.food.ordering.system.customer.service;

import com.food.ordering.system.customer.service.domain.CustomerDomainService;
import com.food.ordering.system.customer.service.domain.CustomerDomainServiceImpl;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomerDomainService customerDomainService(){
        return  new CustomerDomainServiceImpl() ;
    }

}
