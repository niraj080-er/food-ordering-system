package com.food.odering.systsem.oder.service.dataaccess.restaurant.adapter;

import com.food.odering.systsem.oder.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.odering.systsem.oder.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.odering.systsem.oder.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private  final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantDataAccessMapper restaurantDataAccessMapper,
                                    RestaurantJpaRepository restaurantJpaRepository) {
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    /**
     * @param restaurant 
     * @return
     */
    @Override
    public Optional<Restaurant> findRetaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts=
                restaurantDataAccessMapper.restaurantToRestaurantProduct(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities= restaurantJpaRepository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);

        return restaurantEntities.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
    }
}
