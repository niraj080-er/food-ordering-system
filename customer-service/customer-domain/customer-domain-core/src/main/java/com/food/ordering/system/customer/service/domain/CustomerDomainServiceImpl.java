package com.food.ordering.system.customer.service.domain;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerDomainServiceImpl implements CustomerDomainService {

    @Override
    public CustomerCreatedEvent validateAndInitiateCustomer(Customer customer) {
        log.info("Customer with id : {} is iniiated", customer.getId().getValue());
        return new CustomerCreatedEvent(customer, ZonedDateTime.now(ZoneId.of("UTC")));
    }

}