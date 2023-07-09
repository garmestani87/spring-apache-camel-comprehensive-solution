package com.garm.servicegateway.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.garm.servicegateway.exception.ExternalWebServiceException;
import com.garm.servicegateway.exception.GatewayExceptionModel;
import com.garm.servicegateway.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Slf4j
@Component
public abstract class AbstractRestProcessor<I, O> implements Processor, TrustSelfSigned, IExchange<I>, ProcessorExceptionHandler, FormUrlencoded<I> {

    private final Class<I> inClass;
    private final Class<O> outClass;
    private I inObject;

    @Value("${service.have.some.problem}")
    public String callExternalServiceError;

    public AbstractRestProcessor(Class<I> inClass, Class<O> outClass) {
        this.inClass = inClass;
        this.outClass = outClass;
    }

    private void setInObject(I inObject) {
        this.inObject = inObject;
    }

    public I getInObject() {
        return this.inObject;
    }

    protected HttpHeaders setHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

    @Override
    public final void process(Exchange exchange) {
        try {
            if (!Void.class.equals(inClass)) {
                this.setInObject(setExchangeBody(exchange, inClass));
            }
            RestTemplate restTemplate = new RestTemplate();
            String jsonRequest = encode(inObject);
            log.info("***** input url ******" + setTarget(inObject));
            log.info("***** input object ******" + jsonRequest);
            log.info("Url Headers is {} ", this.setHeader());
            disableSSL();
            ResponseEntity<O> response;
            response = restTemplate.exchange(setTarget(inObject), setMethod(),
                    Objects.isNull(inObject) ? new HttpEntity<>(this.setHeader()) : new HttpEntity<>(jsonRequest, this.setHeader()), outClass);

            log.info("***** response ******" + response.getStatusCode());
            if (!outClass.equals(Void.class)) {
                if (Objects.isNull(response.getBody()) || !StringUtils.hasText(response.getBody().toString()))
                    throw new ExternalWebServiceException.NullResponseException(GatewayUtils.buildBody(response));
                GatewayUtils.buildBody(exchange, new ObjectMapper().readValue(new ObjectMapper().writeValueAsBytes(response.getBody()), outClass));
            } else {
                GatewayUtils.buildBody(exchange, null);
            }
        } catch (Throwable throwable) {
            GatewayUtils.handleException(exchange, handleError(throwable));
        }
    }

    public GatewayExceptionModel handleError(Throwable throwable) {
        return new GatewayExceptionModel().setMessage(callExternalServiceError);
    }

    public abstract String setTarget(I inObject);

    public abstract HttpMethod setMethod();

}
