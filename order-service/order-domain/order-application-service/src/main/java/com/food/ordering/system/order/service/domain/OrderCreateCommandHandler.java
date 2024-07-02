package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.exception.DomainExecption;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;


import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.service.domain.OrderDomainService;
import com.food.ordering.system.service.domain.entity.Customer;
import com.food.ordering.system.service.domain.entity.Restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;

    private  final OrderRepository orderRepository;

    private final OrderDataMapper orderDataMapper;

    private final CustomerRepository customerRepository;

    private  final RestaurantRepository restaurantRepository;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     OrderDataMapper orderDataMapper,
                                     CustomerRepository customerRepository,
                                     RestaurantRepository restaurantRepository) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.orderDataMapper = orderDataMapper;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional // This annotation is used to make sure that the transaction is committed only if the method is executed successfully.

    public CreateOrderCommand createOrder(CreateOrderCommand createOrderCommand){
        
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant= checkRestaurant(createOrderCommand);
        
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand)   {
        Restaurant restaurant= orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optinalRestaurant= restaurantRepository.findRetaurantInformation(restaurant);
            if(optinalRestaurant.isEmpty()){
                log.warn("could not find the restaurant with id: {}", createOrderCommand.getRestaurantId());
                throw new DomainExecption("could not find the restaurant with id: "+ createOrderCommand.getRestaurantId());
            }
            return optinalRestaurant.get();

    }

    public void checkCustomer(UUID customerId){
        Optional<Customer> customer = customerRepository.findCustomer( customerId);
        if(!customer.isPresent()){
            log.warn("could not find the customer with id: {}", customerId);
            throw new DomainExecption("could not find the customer with id: "+ customer);
        }
    }
    
}
