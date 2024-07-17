package com.food.ordering.system.payment.service.data.access.creditentry.mapper;

import com.food.ordering.system.payment.service.data.access.creditentry.entity.CreditEntryEntity;
import com.food.odering.system.payment.service.domain.entity.CreditEntity;
import com.food.odering.system.payment.service.domain.valuobject.CreditEntityId;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {

    public CreditEntity creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
        return CreditEntity.Builder.builder()
                .creditEntityId(new CreditEntityId(creditEntryEntity.getId()))
                .customerId(new CustomerId(creditEntryEntity.getCustomerId()))
                .totalCreditAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
                .build();
    }

    public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntity creditEntry) {
        return CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerId().getValue())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount())
                .build();
    }
}