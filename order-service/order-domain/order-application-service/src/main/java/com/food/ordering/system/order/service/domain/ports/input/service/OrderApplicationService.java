package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;

import javax.validation.Valid;

public interface OrderApplicationService {

    CreateOrderCommand  createOrder (@Valid CreateOrderCommand createOrderCommand);
    TrackOrderQuery trackOrder(@Valid  TrackOrderQuery trackOrderQuery);




}
