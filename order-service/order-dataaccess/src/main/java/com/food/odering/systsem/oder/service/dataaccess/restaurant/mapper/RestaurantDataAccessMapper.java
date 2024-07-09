package com.food.odering.systsem.oder.service.dataaccess.restaurant.mapper;

import com.food.odering.systsem.oder.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.odering.systsem.oder.service.dataaccess.restaurant.execption.RestaurantDataAccessMapperException;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestuarantId;
import com.food.ordering.system.service.domain.entity.Product;
import com.food.ordering.system.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantToRestaurantProduct(Restaurant restaurant){

        return restaurant.getProducts().stream().map(product -> product.getId().getValue())
                .collect(Collectors.toList());
    }

    public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities){
        RestaurantEntity restaurantEntity= restaurantEntities.stream().findFirst().orElseThrow(() ->
                new RestaurantDataAccessMapperException("Could not be found! "));

        List<Product> restaurantProduct=restaurantEntities.stream()
                .map(entity-> new Product(new ProductId(entity.getProductID()),entity.getProductName(),new Money(entity.getProductPrice()))).toList();

        return Restaurant.builder()
                .restaurantId(new RestuarantId(restaurantEntity.getRestaurantId()))
                .products(restaurantProduct)
                .active(restaurantEntity.getRestaurantActive())
                .build();
    }
}