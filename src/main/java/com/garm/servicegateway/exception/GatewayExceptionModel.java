package com.garm.servicegateway.exception;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GatewayExceptionModel {
    private String message;
    private Object stackTrace;
    private String code;
    private GatewayExceptionCode exceptionCode;
}
