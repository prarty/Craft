package com.intuit.sride.apigateway.filter;

import com.intuit.sride.apigateway.api.response.AuthorizationResponse;
import com.intuit.sride.apigateway.client.ProfileServiceClient;
import com.intuit.sride.apigateway.exception.ForbiddenResourceException;
import com.intuit.sride.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class AuthorisationFilter extends AbstractGatewayFilterFactory<AuthorisationFilter.Config> {

    final RouteValidator routeValidator;
    final ProfileServiceClient profileServiceClient;
    final JwtUtil jwtUtil;
    private final WebClient.Builder webClientBuilder;

    public AuthorisationFilter(RouteValidator routeValidator, @Lazy ProfileServiceClient profileServiceClient, JwtUtil jwtUtil, WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.profileServiceClient = profileServiceClient;
        this.jwtUtil = jwtUtil;
        this.routeValidator = routeValidator;
        this.webClientBuilder = webClientBuilder;
    }

    // abstract gateway take dependencies in form of webflux input
    @Override
    public GatewayFilter apply(AuthorisationFilter.Config config) {
        return ((exchange, chain) -> {


            if (routeValidator.isSecured.test(exchange.getRequest())) {
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                Claims claims = jwtUtil.getUserClaims(authHeader);
                String username = claims.get("username").toString();
                String userID = claims.get("id").toString();

                return webClientBuilder.build()
                        .get()
                        .uri("http://127.0.0.1:9002/profile/users/{userId}/authorised?resource={resource}&username={username}",
                                        userID, exchange.getRequest().getPath().value(), username)
//                                .queryParam("resource", exchange.getRequest().getPath().value())
//                                .queryParam("username", username)
//                                .build(userID))/**/
                        .retrieve()
                        .bodyToMono(AuthorizationResponse.class)
                        .map(i -> {
                            System.out.println(i.getIsAuthorised());
                            if (!i.getIsAuthorised()) {
                                log.error("Not allowed to access the application");
                                throw new ForbiddenResourceException("unauthorized access to application");
                            }
                            return chain.filter(exchange);
                        }).then();

//                return WebClient.create("http://PROFILE-SERVICE")
//                        .method(HttpMethod.GET)
//                        .uri(builder -> builder.path("/users/{userId}/authorised")
//                                .queryParam("resource", exchange.getRequest().getPath().value())
//                                .queryParam("username", username)
//                                .build(userID))
//                        .retrieve()
//                        .bodyToMono(AuthorizationResponse.class)
//                        .map(i -> {
//                            System.out.println(i.getIsAuthorised());
//                            if (!i.getIsAuthorised()) {
//                                log.error("Not allowed to access the application");
//                                throw new ForbiddenResourceException("unauthorized access to application");
//                            }
//                            return chain.filter(exchange);
//                        }).then();

//                AuthorizationResponse authorised = profileServiceClient.isAuthorised(userID, exchange.getRequest().getPath().value(), username);

//                if (!authorised.getIsAuthorised()) {
//                    log.error("Not allowed to access the application");
//                    throw new ForbiddenResourceException("unauthorized access to application");
//                }
            }
            return chain.filter(exchange);
        });

    }

    public static class Config {
    }
}
