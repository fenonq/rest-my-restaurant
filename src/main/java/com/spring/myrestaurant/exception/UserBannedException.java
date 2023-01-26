package com.spring.myrestaurant.exception;

import com.spring.myrestaurant.model.enums.ErrorType;

public class UserBannedException extends ServiceException {

    private static final String DEFAULT_MESSAGE = "User is banned!";

    public UserBannedException() {
        super(DEFAULT_MESSAGE);
    }

    public UserBannedException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.VALIDATION_ERROR_TYPE;
    }

}
