package com.food.ordering.system.service.doamin.exception;

import com.food.ordering.system.domain.exception.DomainExecption;

public class OrderDoaminException  extends DomainExecption{

    public OrderDoaminException(String message) {
        super(message);
    }
    public OrderDoaminException(String message, Throwable cause) {
        super(message, cause);
    }


}
