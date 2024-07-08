package com.example.gateway.config;

import com.example.gateway.filter.GatewayUriPathLogger;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("authentication-route",p->
                        p.path("/auth/**")
                                .uri("http://localhost:8081"))
                .route("notification-route",path ->
                        path.path("/notification/**")
                        .uri("http://localhost:8082"))
                .route("user-route",p->
                                p.path("/user/**")
                        .uri("http://localhost:8083"))
                .route("user-route",p->
                        p.path("/booking/**")
                                .uri("http://localhost:8084"))
                .build();
    }
    @Bean
    public GlobalFilter globalFilter(){
        return new GatewayUriPathLogger();
    }

}
