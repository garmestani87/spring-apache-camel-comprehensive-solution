package com.garm.servicegateway.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import com.garm.servicegateway.base.Convertible;

import java.util.Arrays;

public enum GatewayExceptionCode implements Convertible<String> {
    ACCOUNT_DETAIL_EXCEPTION("GT-EX-0002");

    private final String value;

    GatewayExceptionCode(String value) {
        this.value = value;
    }

    public GatewayExceptionCode findByValue(String value) {
        return Arrays.stream(values()).filter(exceptionCode -> exceptionCode.value.equals(value)).findFirst().orElse(null);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.value;
    }
}
