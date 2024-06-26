package com.food.ordering.system.service.doamin.entity;

import java.util.List;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.RestuarantId;

public class Restaurat  extends AggregateRoot<RestuarantId>{

    private final List<Product> products;
    private boolean active;

    private Restaurat(Builder builder) {
        super.setId(builder.resturantId);
        products = builder.products;
        active = builder.active;
    }


    public List<Product> getProducts() {
        return products;
    }
    public boolean isActive() {
        return active;
    }


    public static final class Builder {
        private RestuarantId resturantId;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder resturantId(RestuarantId val) {
            resturantId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurat build() {
            return new Restaurat(this);
        }
    }
}
