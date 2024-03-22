package com.app.gateway.filter;

import com.app.gateway.validator.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AuthFilter implements GatewayFilter {

    private static final String AUTH_HEADER = "Authorization";
    private final RouteValidator routerValidator;
    private final WebClient webClient;

    @Value("${urls.validate}")
    private String validateUrl;

    @Autowired
    public AuthFilter(RouteValidator routerValidator, WebClient webClient) {
        this.routerValidator = routerValidator;
        this.webClient = webClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (isAuthMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            return sendValidationRequest(exchange)
                    .flatMap(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return chain.filter(exchange);
                        } else {
                            return onError(exchange, HttpStatus.UNAUTHORIZED);
                        }
                    })
                    .onErrorResume(error -> onError(exchange, HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(AUTH_HEADER);
    }

    private Mono<ClientResponse> sendValidationRequest(ServerWebExchange exchange) {
        String jwt = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);

        return webClient.get()
                .uri(validateUrl)
                .header(AUTH_HEADER, jwt)
                .exchange();
    }
}
