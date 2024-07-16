package com.food.odering.system.payment.service.domain.entity;


import com.food.odering.system.payment.service.domain.valuobject.CreditEntityId;
import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;

public class CreditEntity extends AggregateRoot<CreditEntityId> {

    private  final CustomerId customerId;
    private Money totalCreditAmount;

    private CreditEntity(Builder builder) {
        setId(builder.creditEntityId);
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public void addCreditAmount(Money amount){
        totalCreditAmount=totalCreditAmount.add(amount);
    }
    public  void subtractCreditAmount(Money amount){
        totalCreditAmount=totalCreditAmount.subtract(amount);
    }


    public static final class Builder {
        private CreditEntityId creditEntityId;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder creditEntityId(CreditEntityId val) {
            creditEntityId= val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntity build() {
            return new CreditEntity(this);
        }
    }
    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getTotalCreditAmount() {
        return totalCreditAmount;
    }

}
