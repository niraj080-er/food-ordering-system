package com.food.odering.systsem.oder.service.dataaccess.customer.mapper;


import com.food.odering.systsem.oder.service.dataaccess.customer.entity.CustomerEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerAccessDataMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity){
        return  new Customer(new CustomerId(customerEntity.getId()));
    }
}

