package com.food.ordering.system.service.domain.valueobject;


import com.food.ordering.system.domain.valueobject.BaseId;

public class OrderItemId  extends BaseId<Long>{
    public OrderItemId(long itemId) {
        super(itemId);
    }
    

}
