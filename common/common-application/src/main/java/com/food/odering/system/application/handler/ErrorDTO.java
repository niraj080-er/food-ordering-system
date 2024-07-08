package com.food.odering.system.application.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder

@AllArgsConstructor
@Data
public class ErrorDTO {
    private  final String code;
    private final String message;

}
