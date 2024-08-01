package com.food.ordering.system.domain.event;

public final class EmptyEvent implements DomainEvent <Void>{

    public static EmptyEvent INSTANCE = new EmptyEvent();

    // create a private constructor
    private EmptyEvent() {
        // prevent instantiation from outside this class

    }   


}
