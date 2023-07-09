package com.garm.servicegateway.exception;

import com.garm.servicegateway.base.Response;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ServiceGatewayExceptionHandler extends ResponseEntityExceptionHandler {


    public static ResponseEntity<Object> handleException(BaseException ex, GatewayExceptionCode code) {
        Response<Object, Object> response = new Response<>();
        HttpBaseException exception = new HttpBaseException(ex.getMessage());
        exception.setHttpMessage(ex.getMessage());
        response.setResponse(new GatewayExceptionModel()
                .setMessage(ex.getMessage())
                .setStackTrace(ex.getStackTrace())
                .setExceptionCode(code));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
