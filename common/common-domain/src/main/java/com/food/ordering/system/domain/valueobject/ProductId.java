package com.food.ordering.system.domain.valueobject;

import java.util.UUID;

public class ProductId  extends BaseId<UUID>{

    public ProductId(UUID value) {
        super(value);
    }

    @Override
    public boolean equals(Object obj) {
        
        return super.equals(obj);
    }


    @Override
    public int hashCode() {

        return super.hashCode();
    }
}
