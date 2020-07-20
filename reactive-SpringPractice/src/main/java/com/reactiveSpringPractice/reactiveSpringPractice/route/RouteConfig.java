package com.reactiveSpringPractice.reactiveSpringPractice.route;

import com.reactiveSpringPractice.reactiveSpringPractice.handler.SampleHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfig {
    @Bean
    public RouterFunction<ServerResponse> route(SampleHandlerFunction sampleHandlerFunction) {
        return RouterFunctions.route(RequestPredicates.GET("/function/flux"), sampleHandlerFunction::flux)
                              .andRoute(RequestPredicates.GET("/function/mono"), sampleHandlerFunction::mono);
    }
}
