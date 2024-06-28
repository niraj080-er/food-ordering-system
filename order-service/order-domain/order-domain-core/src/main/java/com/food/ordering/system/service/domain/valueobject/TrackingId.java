package com.food.ordering.system.service.domain.valueobject;

import java.util.UUID;

import com.food.ordering.system.domain.valueobject.BaseId;

public class TrackingId  extends BaseId<UUID>{
    public TrackingId(UUID id) {
        super(id);
    }

}
