package com.intuit.sride.apigateway.filter;


import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.util.context.Context;

import java.util.UUID;

@Log4j2
@Component
public class RequestIDFilter extends AbstractGatewayFilterFactory<RequestIDFilter.Config> {
    private static final String MDC_KEY = "request-id";

    public RequestIDFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            String requestID = UUID.randomUUID().toString(); /* Retrieve the request ID from wherever you store it */

            // Set the MDC context
            return chain.filter(exchange)
                    .contextWrite(Context.of(MDC_KEY, requestID))
                    .doOnTerminate(() -> MDC.remove(MDC_KEY)); // Clear the MDC context after processing
        });
    }

    public static class Config {
    }
}
