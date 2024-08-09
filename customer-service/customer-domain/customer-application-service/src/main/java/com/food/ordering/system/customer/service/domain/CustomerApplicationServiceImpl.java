package com.food.ordering.system.customer.service.domain;


import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.create.CreateCustomerResponse;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import com.food.ordering.system.customer.service.domain.ports.input.service.CustomerApplicationService;
import com.food.ordering.system.customer.service.domain.ports.output.message.publisher.CustomerMessagePublisher;

import lombok.extern.slf4j.Slf4j;

@Validated
@Service
@Slf4j
public class CustomerApplicationServiceImpl implements CustomerApplicationService {
    private final CustomerCreateCommandHandler customerCreateCommandHandler;
    private final CustomerDataMapper customerDataMapper;
    private final CustomerMessagePublisher customerMessagePublisher;

    // create constructor
    public CustomerApplicationServiceImpl(CustomerCreateCommandHandler customerCreateCommandHandler,
            CustomerDataMapper customerDataMapper,
            CustomerMessagePublisher customerMessagePublisher) {
        this.customerCreateCommandHandler = customerCreateCommandHandler;
        this.customerDataMapper = customerDataMapper;
        this.customerMessagePublisher = customerMessagePublisher;
    }

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent event = customerCreateCommandHandler.createCustomer(createCustomerCommand);
        customerMessagePublisher.publish(event);
        return customerDataMapper.customerToCreateCustomerResponse(event.getCustomer(), "Customer saved successfully");

    }

}