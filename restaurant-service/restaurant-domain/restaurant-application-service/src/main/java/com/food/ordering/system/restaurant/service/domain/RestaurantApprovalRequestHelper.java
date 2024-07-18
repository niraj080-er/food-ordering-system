package com.food.ordering.system.restaurant.service.domain;

import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordering.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordering.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordering.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.ordering.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class RestaurantApprovalRequestHelper    {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderApprovalRepository orderApprovalRepository;
    private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;
    private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;


    public RestaurantApprovalRequestHelper(RestaurantDomainService restaurantDomainService,
                                           RestaurantDataMapper restaurantDataMapper,
                                           RestaurantRepository restaurantRepository,
                                           OrderApprovalRepository orderApprovalRepository,
                                           OrderApprovedMessagePublisher orderApprovedMessagePublisher,
                                           OrderRejectedMessagePublisher orderRejectedMessagePublisher) {
        this.restaurantDomainService = restaurantDomainService;
        this.restaurantDataMapper = restaurantDataMapper;
        this.restaurantRepository = restaurantRepository;
        this.orderApprovalRepository = orderApprovalRepository;
        this.orderApprovedMessagePublisher = orderApprovedMessagePublisher;
        this.orderRejectedMessagePublisher = orderRejectedMessagePublisher;
    }

    @Transactional
    public OrderApprovalEvent persistOrderApproval(RestaurantApprovalRequest restaurantApprovalRequest){
        log.info("Processing restaurant approval for order id: {}", restaurantApprovalRequest.getId());
        List<String> failMessage=new ArrayList<>();
        Restaurant restaurant=findRestaurant(restaurantApprovalRequest);
       OrderApprovalEvent orderApprovalEvent=
               restaurantDomainService.validateOrder(restaurant,
                       failMessage,
                       orderApprovedMessagePublisher,
                       orderRejectedMessagePublisher );
    orderApprovalRepository.save(restaurant.getOrderApproval());
    return orderApprovalEvent;

    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant=restaurantDataMapper.
                restaurantApprovalRequestRestaurant(restaurantApprovalRequest);
       Optional<Restaurant> restaurantResult= restaurantRepository.findRestaurantInformation(restaurant);
       if(restaurantResult.isEmpty()){
           log.error("Restaurant not found for with id: {}", restaurant.getId().getValue());
           throw new RestaurantNotFoundException("Restaurant not found for with id: " + restaurant.getId().getValue());
       }
       Restaurant restaurantEntity=restaurantResult.get();
       restaurant.setActive(restaurantEntity.isActive());
       restaurant.getOrderDetail().getProducts(). forEach(product -> {
           restaurantEntity.getOrderDetail().getProducts().forEach(product1 -> {
               if(product1.getId().equals(product.getId())){
                   product.updatePriceWithConfirmedPriceWithAvailability(product1.getName(),product1.getPrice(),product1.isAvailable());
               }
           });
       });
       restaurant.getOrderDetail().setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));
       return restaurant;
    }
}
