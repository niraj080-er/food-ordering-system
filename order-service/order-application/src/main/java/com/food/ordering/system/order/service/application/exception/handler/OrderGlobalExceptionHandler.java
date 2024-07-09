package com.food.ordering.system.order.service.application.exception.handler;

import com.food.odering.system.application.handler.ErrorDTO;
import com.food.odering.system.application.handler.GlobalExceptionHandler;
import com.food.ordering.system.service.domain.exception.OrderDoaminException;
import com.food.ordering.system.service.domain.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



@Slf4j
@ControllerAdvice
public class OrderGlobalExceptionHandler extends GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(value = {OrderDoaminException.class})
public ErrorDTO handleException(OrderDoaminException orderDoaminException){
    log.error(orderDoaminException.getMessage(), orderDoaminException);
    return ErrorDTO.builder().
             code(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(orderDoaminException.getMessage()).
             build();

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(value = {OrderNotFoundException.class})
    public ErrorDTO handleException(OrderNotFoundException orderNotFoundException){
        log.error(orderNotFoundException.getMessage(), orderNotFoundException);
        return ErrorDTO.builder().
                code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(orderNotFoundException.getMessage()).
                build();

    }
}
