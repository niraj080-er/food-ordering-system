package com.food.ordering.system.restaurant.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;
    private final int quantity;
    private boolean isAvailable;

    private Product(Builder builder) {
       setId(builder.productId);
        name = builder.name;
        price = builder.price;
        quantity = builder.quantity;
        isAvailable = builder.isAvailable;
    }

    public static Builder builder() {
        return new Builder();
    }

    public  void  updatePriceWithConfirmedPriceWithAvailability(String name, Money price, boolean
            isAvailable){
        this.name=name;
        this.isAvailable=isAvailable;
        this.price=price;

    }


    public static final class Builder {
        private ProductId productId;
        private String name;
        private Money price;
        private int quantity;
        private boolean isAvailable;

        private Builder() {
        }

        public Builder productId(ProductId val) {
            productId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder isAvailable(boolean val) {
            isAvailable = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
