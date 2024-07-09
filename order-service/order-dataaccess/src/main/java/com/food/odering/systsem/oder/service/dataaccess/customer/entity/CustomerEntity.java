package com.food.odering.systsem.oder.service.dataaccess.customer.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oder_customer_m_view", schema = "customer")
public class CustomerEntity {

    @Id
    private UUID id;

}
