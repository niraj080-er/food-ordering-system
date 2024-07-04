package com.food.ordering.system.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainExecption;

public class OrderNotFoundException  extends DomainExecption

{
    public OrderNotFoundException(String message) {
        super(message);
    }
    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
