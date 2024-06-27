package com.food.ordering.system.service.doamin.valueobject;

import java.util.Objects;
import java.util.UUID;

public class StreetAddress {

     private final UUID id;
     private final String street;
     private final String city;
     private final String postalCode;


        public StreetAddress(UUID id, String street, String city, String postalCode) {
            this.id = id;
            this.street = street;
            this.city = city;
            this.postalCode = postalCode;
        }


        public UUID getId() {
            return id;
        }


        public String getStreet() {
            return street;
        }


        public String getCity() {
            return city;
        }


        public String getPostalCode() {
            return postalCode;
        }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        StreetAddress other = (StreetAddress) obj;
        return Objects.equals(id, other.id) &&
               Objects.equals(street, other.street) &&
               Objects.equals(city, other.city) &&
               Objects.equals(postalCode, other.postalCode);
    }

        @Override
        public int hashCode() {
        return Objects.hash(id, street, city, postalCode);

        }
        
        
}
