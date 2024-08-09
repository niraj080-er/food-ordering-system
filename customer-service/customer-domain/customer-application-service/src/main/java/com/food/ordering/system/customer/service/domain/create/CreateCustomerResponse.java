
package com.food.ordering.system.customer.service.domain.create;

import java.util.UUID;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateCustomerResponse {
    @NotNull
    private UUID customerId;
    @NotNull
    private final String message;
}
