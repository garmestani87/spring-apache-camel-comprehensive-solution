package com.garm.servicegateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public interface FormUrlencoded<I> {
    @SneakyThrows
    default String encode(I inObject) {
        return new ObjectMapper().writeValueAsString(inObject);
    }
}
