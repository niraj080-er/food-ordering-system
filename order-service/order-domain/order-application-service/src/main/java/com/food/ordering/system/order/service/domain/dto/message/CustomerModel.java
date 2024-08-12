package com.food.ordering.system.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CustomerModel {
    private String id;
    private String username;
    private String fistName;
    private String lastName;
}
