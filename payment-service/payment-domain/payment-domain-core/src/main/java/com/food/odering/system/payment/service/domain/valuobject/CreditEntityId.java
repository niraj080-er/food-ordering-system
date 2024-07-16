package com.food.odering.system.payment.service.domain.valuobject;

import com.food.ordering.system.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntityId extends BaseId<UUID> {
    public CreditEntityId(UUID value){
        super(value);
    }

}
