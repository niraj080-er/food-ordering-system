package com.food.ordering.system.order.service.domain;

import org.springframework.stereotype.Service;

import com.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerMessageListenerImpl implements CustomerMessageListener {
    private final CustomerRepository customerRepository;
    private final OrderDataMapper orderDataMapper;

    // generate constructor
    public CustomerMessageListenerImpl(CustomerRepository customerRepository, OrderDataMapper orderDataMapper) {
        this.customerRepository = customerRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Override
    public void customerCreatedEvent(CustomerModel customerModel) {
        Customer customer = customerRepository.save(orderDataMapper.customerModelToCustomer(customerModel));
        if (customer == null) {
            log.error("Customer could not be created in order database with id:{}", customerModel.getId());
            throw new OrderDomainException(
                    "Customer could not be created in order database with id:" + customerModel.getId());
        }
        log.info("Customer created in order database with id:{}", customerModel.getId());
    }
    

}