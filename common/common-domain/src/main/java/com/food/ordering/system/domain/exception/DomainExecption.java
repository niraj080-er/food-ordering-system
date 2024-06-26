package com.food.ordering.system.domain.exception;

public class DomainExecption extends RuntimeException{
    
        public DomainExecption(String message) {
            super(message);
        }

        
        /**
         * @param message
         * @param cause
         */
        public DomainExecption(String message, Throwable cause) {
            super(message, cause);
        }

    }