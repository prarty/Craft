package com.intuit.sride.apigateway.filter;

import com.intuit.sride.apigateway.api.response.AuthorizationResponse;
import com.intuit.sride.apigateway.client.ProfileServiceClient;
import com.intuit.sride.apigateway.exception.ForbiddenResourceException;
import com.intuit.sride.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
@Log4j2
public class AuthorizationFilter implements GlobalFilter, Ordered {

    final JwtUtil jwtUtil;
    final ProfileServiceClient profileServiceClient;
    final RouteValidator routeValidator;


    public AuthorizationFilter(JwtUtil jwtUtil, ProfileServiceClient profileServiceClient, RouteValidator routeValidator) {
        this.jwtUtil = jwtUtil;
        this.profileServiceClient = profileServiceClient;
        this.routeValidator = routeValidator;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (routeValidator.isSecured.test(exchange.getRequest())) {
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }
            Claims claims = jwtUtil.getUserClaims(authHeader);
            String username = claims.get("username").toString();
            String userID = claims.get("id").toString();
            AuthorizationResponse authorised = profileServiceClient.isAuthorised(userID, exchange.getRequest().getPath().value(), username);

            if (!authorised.getIsAuthorised()) {
                log.error("Not allowed to access the application");
                throw new ForbiddenResourceException("unauthorized access to application");
            }
        }
        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
