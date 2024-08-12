package com.food.ordering.system.customer.service.domain;


import org.springframework.stereotype.Component;

import com.food.ordering.system.customer.service.domain.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.customer.service.domain.event.CustomerCreatedEvent;
import com.food.ordering.system.customer.service.domain.exception.CustomerDomainException;
import com.food.ordering.system.customer.service.domain.mapper.CustomerDataMapper;
import com.food.ordering.system.customer.service.domain.ports.output.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class CustomerCreateCommandHandler {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;
    private final CustomerDataMapper customerDataMapper;

    // create constructor
    public CustomerCreateCommandHandler(CustomerDomainService customerDomainService,
            CustomerRepository customerRepository,
            CustomerDataMapper customerDataMapper) {
        this.customerDomainService = customerDomainService;
        this.customerRepository = customerRepository;
        this.customerDataMapper = customerDataMapper;
    }

    @Transactional
    public CustomerCreatedEvent createCustomer(CreateCustomerCommand customerCreateCommand) {
        Customer customer = customerDataMapper.createCustomerCommandToCustomer(customerCreateCommand);
        CustomerCreatedEvent event = customerDomainService.validateAndInitiateCustomer(customer);
        Customer saveCustomer = customerRepository.createCustomer(customer);
        if (saveCustomer == null) {
            log.error("Customer could  not save with id: {} ", customerCreateCommand.getCustomerId());
            throw new CustomerDomainException(
                    "Customer could  not save with id: " + customerCreateCommand.getCustomerId());
        }
        log.info("Customer could save with id: {} ", customerCreateCommand.getCustomerId());
        return event;

    }
}
