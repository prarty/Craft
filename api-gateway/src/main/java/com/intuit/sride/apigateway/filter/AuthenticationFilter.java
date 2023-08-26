package com.intuit.sride.apigateway.filter;

import com.intuit.sride.apigateway.exception.ForbiddenResourceException;
import com.intuit.sride.apigateway.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    final RouteValidator routeValidator;

    final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator routeValidator, JwtUtil jwtUtil) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtUtil = jwtUtil;
    }

    // abstract gateway take dependencies in form of webflux input
    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return ((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.error("Missing authorization token");
                    throw new ForbiddenResourceException("Missing authorization token");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    log.error("unauthorized access to application");
                    throw new ForbiddenResourceException("unauthorized access to application");
                }
            }
            return chain.filter(exchange);
        });

    }

    public static class Config {
    }
}
