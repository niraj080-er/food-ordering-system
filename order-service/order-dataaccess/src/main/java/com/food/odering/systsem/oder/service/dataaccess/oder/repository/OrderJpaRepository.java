package com.food.odering.systsem.oder.service.dataaccess.oder.repository;

import com.food.odering.systsem.oder.service.dataaccess.oder.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

    import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByTrackingID(UUID trackingId);


}
