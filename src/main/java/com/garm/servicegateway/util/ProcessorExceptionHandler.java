package com.garm.servicegateway.util;

import com.garm.servicegateway.exception.GatewayExceptionModel;

public interface ProcessorExceptionHandler {
    default GatewayExceptionModel handleError(Throwable throwable) {
        return new GatewayExceptionModel().setMessage(throwable.getMessage());
    }
}
