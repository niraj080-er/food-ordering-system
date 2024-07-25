package com.food.ordering.system.application.handler;

import lombok.Data;

@Data
public class ErrorDTO {
    
    private final String message;
    private final String code;
    
	public ErrorDTO(String message, String code) {
		super();
		this.message = message;
		this.code = code;
	} 
	
	private ErrorDTO(Builder builder) {
        code = builder.code;
        message = builder.message;
    }


    public static final class Builder {
        private String code;
        private String message;

        private Builder() {
        }

        public Builder code(String val) {
            code = val;
            return this;
        }

        public Builder message(String val) {
            message = val;
            return this;
        }

        public ErrorDTO build() {
            return new ErrorDTO(this);
        }
    }


	public static Builder builder() {
		return new Builder();
	}
}