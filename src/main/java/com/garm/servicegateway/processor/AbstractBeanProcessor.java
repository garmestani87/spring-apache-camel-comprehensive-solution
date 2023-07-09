package com.garm.servicegateway.processor;

import com.garm.servicegateway.util.GatewayUtils;
import com.garm.servicegateway.util.IExchange;
import com.garm.servicegateway.util.ProcessorExceptionHandler;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public abstract class AbstractBeanProcessor<I, O> implements Processor, IExchange<I>, ProcessorExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBeanProcessor.class);
    private final Class<I> inClass;
    private final Class<O> outClass;
    private I inObject;

    public AbstractBeanProcessor(Class<I> inClass, Class<O> outClass) {
        this.inClass = inClass;
        this.outClass = outClass;
    }

    private void setInObject(I inObject) {
        this.inObject = inObject;
    }

    public I getInObject() {
        return this.inObject;
    }

    @Override
    public final void process(Exchange exchange) {
        try {
            if (!Void.class.equals(inClass)) {
                this.setInObject(setExchangeBody(exchange, inClass));
            }
            GatewayUtils.buildBody(exchange, doProcess(inObject));
        } catch (Throwable throwable) {
            GatewayUtils.handleException(exchange, handleError(throwable));
        }
    }

    public abstract O doProcess(I inObject);

}
