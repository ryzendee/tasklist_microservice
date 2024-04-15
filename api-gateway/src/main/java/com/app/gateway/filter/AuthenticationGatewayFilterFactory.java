package com.app.gateway.filter;

import com.app.gateway.validator.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
    private final RouteValidator routerValidator;
    private final RestTemplate restTemplate;

    @Value("${urls.validate}")
    private String validateUrl;

    @Autowired
    public AuthenticationGatewayFilterFactory(RouteValidator routerValidator, RestTemplate restTemplate) {
        super(Config.class);
        this.routerValidator = routerValidator;
        this.restTemplate = restTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (routerValidator.isSecured.test(request)) {
                if (isAuthMissing(request)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                try {
                    ResponseEntity<Void> response = sendValidationRequest(exchange);
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return chain.filter(exchange);
                    } else {
                        return onError(exchange, HttpStatus.UNAUTHORIZED);
                    }
                } catch (RestClientException ex) {
                    return onError(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(AUTH_HEADER);
    }

    private ResponseEntity<Void> sendValidationRequest(ServerWebExchange exchange) {
        log.info("Sending validation request to auth-service: {}", validateUrl);

        String jwt = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);

        HttpHeaders headers = new HttpHeaders();
        headers.set(AUTH_HEADER, jwt);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(validateUrl, HttpMethod.POST, requestEntity, Void.class);

    }

    public static class Config {
    }
}
