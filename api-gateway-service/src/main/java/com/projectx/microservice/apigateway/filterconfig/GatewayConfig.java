package com.projectx.microservice.apigateway.filterconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    RequestAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("category-service", r -> r.path("/api/category/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://CATEGORY-SERVICE"))
                .route("product-service", r -> r.path("/api/product/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://PRODUCT-SERVICE"))
                .route("inventory-service", r -> r.path("/api/inventoryStock/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://INVENTORY-SERVICE"))
                .route("authentication-service", r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTHENTICATION-SERVICE"))
                .route("order-service", r -> r.path("/api/orders/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ORDER-SERVICE"))
                .route("payment-service", r -> r.path("/api/payment/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://PAYMENT-SERVICE"))
                .route("admin-service",r -> r.path("/api/discount/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ADMIN-SERVICE"))
                .build();
    }
}
