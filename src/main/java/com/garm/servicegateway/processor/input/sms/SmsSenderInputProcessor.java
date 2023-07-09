package com.garm.servicegateway.processor.input.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garm.servicegateway.model.SmsPanelDto;
import com.garm.servicegateway.processor.AbstractRestProcessor;
import com.garm.servicegateway.util.GatewayUtils;
import lombok.SneakyThrows;
import org.apache.camel.Exchange;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmsSenderInputProcessor extends AbstractRestProcessor<List, List> {

    private String url;

    private String subsystem;

    public SmsSenderInputProcessor() {
        super(List.class, List.class);
    }

    @Override
    public String setTarget(List inObject) {
        return url;
    }

    @Override
    public HttpMethod setMethod() {
        return HttpMethod.POST;
    }

    @SneakyThrows
    @Override
    protected HttpHeaders setHeader() {
        HttpHeaders headers = new HttpHeaders();
        SmsPanelDto smsDto = GatewayUtils.getObjectFromResource("/sms_provider.properties", subsystem + ".", SmsPanelDto.class);
        headers.setBasicAuth(smsDto.getUsername(), smsDto.getPassword());
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return headers;
    }

    @SneakyThrows
    @Override
    public List setExchangeBody(Exchange exchange, Class<List> clazz) {
        this.subsystem = exchange.getIn().getHeader("subsystem").toString();
        return new ObjectMapper().readValue(exchange.getIn().getBody(String.class), clazz);
    }
}
