package com.food.odering.system.payment.service.domain.entity;

import com.food.odering.system.payment.service.domain.valuobject.PaymentId;
import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.*;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class Payment extends AggregateRoot<PaymentId> {
    private final OrderId orderId;
    private  final CustomerId customerId;
    private final Money price;
    private PaymentStatus paymentStatus;
    private ZonedDateTime createdAt;

//    private Payment(Builder builder) {
//        setId(builder.paymentId);
//        orderId = builder.orderId;
//        customerId = builder.customerId;
//        price = builder.price;
//        paymentStatus = builder.paymentStatus;
//        createdAt = builder.createdAt;
//    }
//
    public  void initializePayment(){
        setId(new PaymentId(UUID.randomUUID()));
        createdAt=ZonedDateTime.now(ZoneId.of("UTC"));
    }
    public void validatePayment(List<String> failureMessages){
        if(price == null || !price.isGreaterThanZero()) {
            failureMessages.add("Total price must be greater than zero");
        }
    }

    public void updateStatus(PaymentStatus paymentStatus){
        this.paymentStatus=paymentStatus;
    }

//
//    public OrderId getOrderId() {
//        return orderId;
//    }
//
//    public CustomerId getCustomerId() {
//        return customerId;
//    }
//
//    public Money getPrice() {
//        return price;
//    }
//
//    public PaymentStatus getPaymentStatus() {
//        return paymentStatus;
//    }
//
//    public ZonedDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public static final class Builder {
//        private PaymentId paymentId;
//        private OrderId orderId;
//        private CustomerId customerId;
//        private Money price;
//        private PaymentStatus paymentStatus;
//        private ZonedDateTime createdAt;
//
//        private Builder() {
//        }
//
//        public static Builder builder() {
//            return new Builder();
//        }
//
//        public Builder paymentId(PaymentId val) {
//            paymentId = val;
//            return this;
//        }
//
//        public Builder orderId(OrderId val) {
//            orderId = val;
//            return this;
//        }
//
//        public Builder customerId(CustomerId val) {
//            customerId = val;
//            return this;
//        }
//
//        public Builder price(Money val) {
//            price = val;
//            return this;
//        }
//
//        public Builder paymentStatus(PaymentStatus val) {
//            paymentStatus = val;
//            return this;
//        }
//
//        public Builder createdAt(ZonedDateTime val) {
//            createdAt = val;
//            return this;
//        }
//
//        public Payment build() {
//            return new Payment(this);
//        }
//    }
}