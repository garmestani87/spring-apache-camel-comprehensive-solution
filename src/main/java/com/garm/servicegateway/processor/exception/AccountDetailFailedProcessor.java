package com.garm.servicegateway.processor.exception;

import com.garm.servicegateway.exception.ExternalWebServiceException;
import com.garm.servicegateway.exception.GatewayExceptionCode;
import com.garm.servicegateway.exception.ServiceGatewayExceptionHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailFailedProcessor implements Processor {

    private final Gson gson = new GsonBuilder().serializeNulls().create();

    @Override
    public void process(Exchange exchange) throws Exception {
        Throwable t = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        ExternalWebServiceException.AccountDetailFailedException accountDetailFailedException;
        if (t != null) {
            accountDetailFailedException = (ExternalWebServiceException.AccountDetailFailedException) t;
        } else {
            accountDetailFailedException = new ExternalWebServiceException.AccountDetailFailedException("service is down ...");
        }

        exchange.getIn().setBody(gson.toJson(ServiceGatewayExceptionHandler.handleException(accountDetailFailedException, GatewayExceptionCode.ACCOUNT_DETAIL_EXCEPTION)));
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.OK);
    }
}