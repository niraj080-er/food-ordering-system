package com.food.ordering.system.order.service.domain;


import com.food.ordering.system.domain.exception.DomainExecption;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.service.domain.OrderDomainService;
import com.food.ordering.system.service.domain.entity.Customer;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Restaurant;
import com.food.ordering.system.service.domain.entity.Transactional;
import com.food.ordering.system.service.domain.events.OrderCreateEvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class OrderCreateHelper {


    private final OrderDomainService orderDomainService;

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantRepository restaurantRepository;

    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             OrderRepository orderRepository,
                             CustomerRepository customerRepository,
                             RestaurantRepository restaurantRepository,
                             OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreateEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        @SuppressWarnings("unused")
        OrderCreateEvent orderCreatedEvent = orderDomainService.validateItemAndInitiateOrder(order, restaurant);
        log.info("order created");
        saveOrder(order);
        return orderCreatedEvent;
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCammandToRestaurant(createOrderCommand);
        Optional<Restaurant> optinalRestaurant = restaurantRepository.findRetaurantInformation(restaurant);
        if (optinalRestaurant.isEmpty()) {
            log.warn("could not find the restaurant with id: {}", createOrderCommand.getRestaurantId());
            throw new DomainExecption("could not find the restaurant with id: " + createOrderCommand.getRestaurantId());
        }
        return optinalRestaurant.get();

    }


    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private Order saveOrder (Order order){
        Order result = orderRepository.save(order);
        if (result == null) {
            log.warn("could not save the order ");
            throw new DomainExecption("could not save the order ");
        }
        log.info("ORder saved with id {}", result.getId().getValue());
        return result;
    }


    public void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);
        if (!customer.isPresent()) {
            log.warn("could not find the customer with id: {}", customerId);
            throw new DomainExecption("could not find the customer with id: " + customer);
        }  
    }
}