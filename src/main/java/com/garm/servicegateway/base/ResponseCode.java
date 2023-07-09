package com.garm.servicegateway.base;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseCode implements Convertible<Long> {
    GENERAL("GENERAL", 0L),
    EXCEPTION("EXCEPTION", 1L),
    UNAUTHORIZED("UNAUTHORIZED", 401L);

    private String value;
    private Long code;

    ResponseCode(String value, Long code) {
        this.value = value;
        this.code = code;
    }

    @Override
    @JsonValue
    public Long getValue() {
        return this.code;
    }
}