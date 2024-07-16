package com.food.ordering.system.payment.service.domain.ports.output.repository;

import com.food.odering.system.payment.service.domain.entity.CreditEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;

import java.util.Optional;
import java.util.UUID;

public interface CreditEntryRepository {
    CreditEntity save(CreditEntity creditEntity);
    Optional<CreditEntity> findByCustomerId(CustomerId customerId);


}
