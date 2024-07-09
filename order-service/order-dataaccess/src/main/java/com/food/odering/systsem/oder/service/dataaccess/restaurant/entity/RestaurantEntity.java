package com.food.odering.systsem.oder.service.dataaccess.restaurant.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RestaurantEntityId.class)
@Table(name = "order_restaurant_m_view", schema = "restaurant")
public class RestaurantEntity {

    @Id
    private UUID restaurantId;
    @Id
    private  UUID productID;

    private  String restaurantName;
    private  Boolean restaurantActive;
    private  String productName;
    private BigDecimal productPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantEntity that = (RestaurantEntity) o;
        return Objects.equals(restaurantId, that.restaurantId) && Objects.equals(productID, that.productID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restaurantId, productID);
    }
}
