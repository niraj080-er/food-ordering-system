package com.food.odering.systsem.oder.service.dataaccess.customer.adapter;

import com.food.odering.systsem.oder.service.dataaccess.customer.mapper.CustomerAccessDataMapper;
import com.food.odering.systsem.oder.service.dataaccess.customer.repository.CustomerJpaRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.service.domain.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private  final CustomerJpaRepository  customerJpaRepositoryRepository;
    private  final CustomerAccessDataMapper customerAccessDataMapper;


    public CustomerRepositoryImpl(CustomerAccessDataMapper customerAccessDataMapper, CustomerJpaRepository customerJpaRepositoryRepository) {
        this.customerAccessDataMapper = customerAccessDataMapper;
        this.customerJpaRepositoryRepository = customerJpaRepositoryRepository;
    }

    /**
     * @param customerId 
     * @return
     */
    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJpaRepositoryRepository.findById(customerId).map(customerAccessDataMapper::customerEntityToCustomer);

    }
}
