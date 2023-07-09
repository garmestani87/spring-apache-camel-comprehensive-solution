package com.garm.servicegateway.routebuilder;

import com.garm.servicegateway.processor.input.sms.SmsSenderInputProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestExampleRouteBuilder extends RouteBuilder {

    private final SmsSenderInputProcessor input;

    @Override
    public void configure() {
        addRoute(input);
    }

    private void addRoute(SmsSenderInputProcessor processor) {
        from("rest:put:external/send-sms/send/{subsystem}")
                .process(processor)
                .marshal().
                json(JsonLibrary.Jackson, String.class);
    }
}
