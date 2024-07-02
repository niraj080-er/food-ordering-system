package com.food.ordering.system.order.service.domain.mapper;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.service.domain.entity.OrderItem;
import com.food.ordering.system.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestuarantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.service.domain.entity.Order;
import com.food.ordering.system.service.domain.entity.Product;
import com.food.ordering.system.service.domain.entity.Restaurant;

@Component

public class OrderDataMapper {

    public Restaurant createOrderCammandToRestaurant(CreateOrderCommand createOrderCommand){
        return Restaurant.builder()
        .restaurantId(new RestuarantId(createOrderCommand.getRestaurantId()))
        .products(createOrderCommand.getItems().stream().map(orderItem->
        new Product(new ProductId(orderItem.getProductId())))
        .collect(Collectors.toList()))
        .build();
                 
    }

public Order createOrderCammandToOrder(CreateOrderCommand createOrderCommand){

        return Order.builder()
        .customerId(new CustomerId(createOrderCommand.getCustomerId()))
        .restaurantId(new RestuarantId(createOrderCommand.getRestaurantId()))
        .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
        .price(new Money(createOrderCommand.getPrice()))
        .items(orderItemToOrderItemEntity(createOrderCommand.getItems()))
        .build();
}

    @SuppressWarnings("unchecked")
    private List<OrderItem> orderItemToOrderItemEntity(List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> orderItems) {
            return orderItems.stream()
                    .map(orderItem -> OrderItem.builder()
                    .products(new Product(new ProductId(orderItem.getProductId())))
                    .price(new Money(orderItem.getPrice()))
                    .quantity(orderItem.getQuantity())
                    .subTotal(new Money(orderItem.getSubTotal()))
                    .build()).collect(Collectors.toList());


    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.getStreet(),
                address.getCity(),
                address.getPostalCode()
        );
    }


}