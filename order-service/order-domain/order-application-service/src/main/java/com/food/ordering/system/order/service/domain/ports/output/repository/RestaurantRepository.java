package com.food.ordering.system.order.service.domain.ports.output.repository;

import com.food.ordering.system.service.domain.entity.Restaurant;


import java.util.Optional;

public interface RestaurantRepository {

  // Restaurant  save(Restaurant restaurant);

  Optional<Restaurant> findRetaurantInformation(Restaurant restaurant);
}
