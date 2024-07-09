package com.food.odering.systsem.oder.service.dataaccess.restaurant.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntityId implements Serializable {

    private UUID restaurantID;
    private  UUID productId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntityId that = (RestaurantEntityId) o;
        return Objects.equals(restaurantID, that.restaurantID) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantID, productId);
    }
}
