package com.food.ordering.system.service.doamin.entity;

import java.util.List;
import java.util.UUID;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.domain.valueobject.RestuarantId;
import com.food.ordering.system.service.doamin.exception.OrderDoaminException;
import com.food.ordering.system.service.doamin.valueobject.OrderItemId;
import com.food.ordering.system.service.doamin.valueobject.StreetAddress;
import com.food.ordering.system.service.doamin.valueobject.TrackingId;

public class Order extends AggregateRoot<OrderId> {

    private final CustomerId customerId;
    private final RestuarantId resturantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super.setId(builder.orderId);
        customerId = builder.customerId;
        resturantId = builder.resturantId;
        deliveryAddress = builder.deliveryAddress;
        price = builder.price;
        items = builder.items;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }
    public RestuarantId getResturantId() {
        return resturantId;
    }
    public StreetAddress getDeliveryAddress() {
        return deliveryAddress;
    }
    public Money getPrice() {
        return price;
    }
    public List<OrderItem> getItems() {
        return items;
    }
    public TrackingId getTrackingId() {
        return trackingId;
    }
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    public List<String> getFailureMessages() {
        return failureMessages;
    }


    public static final class Builder {
        private  OrderId orderId;
        private CustomerId customerId;
        private RestuarantId resturantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> items;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;



        public void validateOrder() {
            validateInitialOrder();
            validateTotalPrice();
            validateItemPrice();
        }

        public void pay(){
            if(orderStatus != OrderStatus.PENDING){
                throw new OrderDoaminException("Order is not in correct state for payment");
            }
            orderStatus = OrderStatus.PAID;
        }

        public void approve(){
            if(orderStatus != OrderStatus.PAID ){
                throw new OrderDoaminException("Order is not in correct state for approval");
            }
            orderStatus = OrderStatus.APPROVED;

        }

        public void initCencle(List<String> failureMessages){
            if(orderStatus != OrderStatus.PAID){
                throw new OrderDoaminException("Order is not in correct state for cancelation");
            }
            orderStatus = OrderStatus.CANCELING;
            updateFailureMessage(failureMessages);
        }
        

        public void cencle(List<String> failureMessage){
            if(!(orderStatus == OrderStatus.CANCELLED || orderStatus==OrderStatus.PENDING)){
                throw new OrderDoaminException("Order is not in corret state for initilization");
            }
            orderStatus=OrderStatus.CANCELLED;
            updateFailureMessage(failureMessage);

        }
        public void updateFailureMessage(List<String> failureMessages ){

            if(this.failureMessages != null && failureMessages != null){
                    this.failureMessages.addAll(failureMessages.stream().filter(message->!message.isEmpty()).toList());
            }
            if(this.failureMessages==null){
                this.failureMessages=failureMessages;
            }

        }

 

        public void validateInitialOrder(){
            if(orderStatus== null || orderId == null){
                throw new OrderDoaminException("Order is not in correct state for initilization");
            }
        }

        
        private void validateTotalPrice(){
            if(price == null || !price.isGreaterThan()){
                throw new OrderDoaminException("Order total price must be greaterthan zero ");
            }

           
            
        }
        
           private void validateItemPrice(){
           Money orerItemTotal=items.stream().map(orderItem->{
           validateItemPrice(orderItem);
           return orderItem.getSubTotal();


           }).reduce(Money.ZERO, Money::add);

           if(!price.equals(orerItemTotal)){
            throw new OrderDoaminException("Total price " + 
            price.getAmount() + " is not equal to sum of order items price " + 
            orerItemTotal.getAmount());
           }
        }

        private void validateItemPrice(OrderItem orderItem){
        if(!orderItem.ispriceValue()){
             throw new OrderDoaminException("Order Item price : " + 
             orderItem.getPrice().getAmount() + " is not valid for product  : " + 
             orderItem.getProduct().getId().getValue() );
        }
    }

        public void intializeOrder() {
            orderID(new OrderId(UUID.randomUUID()));
            trackingId  = new TrackingId(UUID.randomUUID());
            orderStatus = OrderStatus.PENDING;
            intializeOrderItems();
        }

        // private void intializeOrderItems() {
        //     items.forEach(item -> {
        //         final long finalI = 1;
        //         item.intializeOrderItem(orderId, new OrderItemId(finalI));
        //     });
        // } 

        public void intializeOrderItems(){
                long i=1;
                for(OrderItem item: items){
                    item.intializeOrderItem(orderId, new OrderItemId(i++));
                }
        }
        
        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder orderID(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder resturantId(RestuarantId val) {
            resturantId = val;
            return this;
        }

        public Builder deliveryAddress(StreetAddress val) {
            deliveryAddress = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder items(List<OrderItem> val) {
            items = val;
            return this;
        }

        public Builder trackingId(TrackingId val) {
            trackingId = val;
            return this;
        }

        public Builder orderStatus(OrderStatus val) {
            orderStatus = val;
            return this;
        }

        public Builder failureMessages(List<String> val) {
            failureMessages = val;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }


    
}
