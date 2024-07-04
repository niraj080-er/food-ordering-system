package com.food.ordering.system.order.service.domain.mapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.service.domain.entity.OrderItem;
import com.food.ordering.system.service.domain.valueobject.StreetAddress;

import org.springframework.stereotype.Component;
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
        new Product(new ProductId(orderItem.getProductId()))).collect(Collectors.toList()))
        .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestuarantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemsEntities(createOrderCommand.getItems()))
                // .trackingId(new TrackingId(UUID.randomUUID()))
                // .orderStatus(OrderStatus.PENDING)
                // .failureMessages(Collections.emptyList()) this will be added in the OrderDomainService
                .build();
    }
    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.getStreet(),
                address.getCity(),
                address.getPostalCode()
        );

    }

    private List<OrderItem> orderItemsToOrderItemsEntities(
        List<com.food.ordering.system.order.service.domain.dto.create.OrderItem> orderItems
    ) {
        return orderItems.stream()
            .map(orderItem -> OrderItem.builder()
                .product(new Product(new ProductId(orderItem.getProductId())))
                .quantity(orderItem.getQuantity())
                .build())
            .collect(Collectors.toList());
}

public TrackOrderResponse orderToTrackOrderResponse(Order order,String string) {
    return TrackOrderResponse.builder()
            .orderTrackingId(order.getTrackingId().getValue())
            .orderStatus(order.getOrderStatus())
            .failureMessages(order.getFailureMessages())
            .build();
}
}