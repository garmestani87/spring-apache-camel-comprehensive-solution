package com.garm.servicegateway.processor.output;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class AccountDetailsOutputProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // TODO: 09.07.23   set your response here for example like below code
        //AccountDetailsResponse response = exchange.getIn().getBody(AccountDetailsResponse.class);
        //if (Objects.isNull(response)) {
        //    throw new HttpBaseException("response is null ...");
        //}
        //exchange.getIn().setBody(response);
    }
}