package com.intuit.sride.apigateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenResourceException extends RuntimeException{

	public ForbiddenResourceException(String message){
		super(message);
	}

}
