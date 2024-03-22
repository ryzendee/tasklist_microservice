package com.app.gateway.filter;

import com.app.gateway.validator.RouteValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
    private final RouteValidator routerValidator;
    private final WebClient webClient;

    @Value("${urls.validate}")
    private String validateUrl;

    @Autowired
    public AuthenticationGatewayFilterFactory(RouteValidator routerValidator, WebClient webClient) {
        super(Config.class);
        this.routerValidator = routerValidator;
        this.webClient = webClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (routerValidator.isSecured.test(request)) {
                if (isAuthMissing(request)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                return sendValidationRequest(exchange)
                        .flatMap(response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return chain.filter(exchange).then(Mono.empty());
                            } else {
                                return onError(exchange, HttpStatus.UNAUTHORIZED);
                            }
                        })
                        .onErrorResume(error -> onError(exchange, HttpStatus.INTERNAL_SERVER_ERROR));
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

    private Mono<ClientResponse> sendValidationRequest(ServerWebExchange exchange) {
        log.info("Sending validation request to auth-service: {}", validateUrl);

        String jwt = exchange.getRequest().getHeaders().getFirst(AUTH_HEADER);
        return webClient.post()
                .uri(validateUrl)
                .header(AUTH_HEADER, jwt)
                .retrieve()
                .bodyToMono(ClientResponse.class);
    }

    public static class Config {
    }
}
