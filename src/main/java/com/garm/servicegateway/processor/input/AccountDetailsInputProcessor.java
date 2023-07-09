package com.garm.servicegateway.processor.input;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailsInputProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // TODO: 09.07.23   set your request here for example like below code
        // AccountDetailsRequest request = new Gson().fromJson(exchange.getIn().getBody(String.class), AccountDetailsRequest.class);
        // exchange.getIn().setBody(request);
    }
}