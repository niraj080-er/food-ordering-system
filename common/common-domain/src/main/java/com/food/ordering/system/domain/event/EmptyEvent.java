package com.food.ordering.system.domain.event;

public final class EmptyEvent implements DomainEvent <Void>{

    public static EmptyEvent INSTANCE = new EmptyEvent();

    @Override
    public void fire() {
        // do nothing
        // useful for empty events
        // e.g., in a saga where there is no business logic to execute
    }
    // create a private constructor
    private EmptyEvent() {
        // prevent instantiation from outside this class

    }   


}
