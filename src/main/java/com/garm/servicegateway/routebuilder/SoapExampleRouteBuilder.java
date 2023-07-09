package com.garm.servicegateway.routebuilder;

import com.garm.servicegateway.exception.ExternalWebServiceException;
import com.garm.servicegateway.processor.exception.AccountDetailFailedProcessor;
import com.garm.servicegateway.processor.input.AccountDetailsInputProcessor;
import com.garm.servicegateway.processor.output.AccountDetailsOutputProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SoapExampleRouteBuilder extends RouteBuilder {

    private final Environment env;
    private final AccountDetailsInputProcessor accountDetailsInputProcessor;
    private final AccountDetailsOutputProcessor accountDetailsOutputProcessor;
    private final AccountDetailFailedProcessor accountDetailFailedProcessor;

    @Override
    public void configure() {

        onException(ExternalWebServiceException.AccountDetailFailedException.class)
                .useOriginalMessage()
                .process(accountDetailFailedProcessor)
                .handled(true);

        addRoute("getAccountDetails", accountDetailsInputProcessor, accountDetailsOutputProcessor);
        addTestRoutes();
    }

    private void addRoute(String operationName, Processor input, Processor output) {
        from("rest:post:external/accounts/" + operationName)
                .process(input)
                .routeId("route_accounts_details_" + operationName)
                .setHeader(CxfConstants.OPERATION_NAME, simple(env.getProperty("account.details.operation.name." + operationName)))
                .setHeader(CxfConstants.OPERATION_NAMESPACE, simple(env.getProperty("account.namespace")))
                .toD("cxf://" + env.getProperty("account.service.url") +
                        "?serviceClass=" + env.getProperty("account.service.class") +
                        "&wsdlURL=" + env.getProperty("account.service.wsdl.url"))
                .process(output)
                .marshal().json(JsonLibrary.Jackson);
    }

    private void addTestRoutes() {
        from("rest:post:external/accounts/getAccountDetails/test")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        //todo set your response here for example like below code

                        // AccountDetailsResponse body = new AccountDetailsResponse();
                        // Account account = new Account();
                        // account.setAccountNumber("12345678");
                        // account.setAccountStatus(EnumAccountStatus.ACTIVE);
                        // account.setAccountName("mohsen garmestani");
                        // account.setAccountBalance(2500);
                        // body.setAccountDetails(account);
                        // exchange.getIn().setBody(body);
                    }
                })
                .marshal().json(JsonLibrary.Jackson);
    }
}
