package com.garm.servicegateway.base;

import com.garm.servicegateway.exception.GatewayExceptionModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class Response<INPUT, OUTPUT> {
    private INPUT inputArguments;
    private OUTPUT response;
    private ResponseCode responseCode = ResponseCode.GENERAL;
    private GatewayExceptionModel errorDetail;
}