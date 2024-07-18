package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.restaurant.service.domain.valueobject.OrderApprovalId;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
    private OrderApproval orderApproval;
    private boolean active;
    private  final  OrderDetail orderDetail;

    private Restaurant(Builder builder) {
        setId(builder.restaurantId);
        orderApproval = builder.orderApproval;
        setActive(builder.active);
        orderDetail = builder.orderDetail;
    }

    public void validateOrder(List<String> failureMessage){
        if(orderDetail.getStatus()!= OrderStatus.PAID){
            failureMessage.add("Payment is not completed for this Order : " + orderDetail.getId());
        }
        Money totalAmount=orderDetail.getProducts().stream().map(product -> {
        if(!product.isAvailable()){
            failureMessage.add("Product with id "+ product.getId().getValue() + " is not available");
        }
            return product.getPrice().multiply(product.getQuantity());
        }).reduce(Money.ZERO,Money::add);

        if(!totalAmount.equals(orderDetail.getTotalAmount())){
            failureMessage.add("Total price must be same as  "+ orderDetail.getId());
        }
    }

    public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus){
        this.orderApproval=OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .restaurantId(this.getId())
                .orderId(this.getOrderDetail().getId())
                .orderApprovalStatus(orderApprovalStatus)
                .build();
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private OrderApproval orderApproval;
        private boolean active;
        private OrderDetail orderDetail;

        private Builder() {
        }


        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
