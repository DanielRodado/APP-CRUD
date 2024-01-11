package com.mindhub.AppCrud.utils;

public final class ValidationExceptionUtil {

    public static RuntimeException validationException(String message) {
        return new RuntimeException(message);
    }

}
