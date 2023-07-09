package com.garm.servicegateway.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;

public interface IExchange<I> {

    @SneakyThrows
    default I setExchangeBody(Exchange exchange, Class<I> clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return objectMapper.readValue(exchange.getIn().getBody(String.class), clazz);
    }

}
