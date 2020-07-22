package com.reactiveSpringPractice.reactiveSpringPractice.route;

import com.reactiveSpringPractice.reactiveSpringPractice.constant.ItemConstant;
import com.reactiveSpringPractice.reactiveSpringPractice.handler.ItemHandlerFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ItemRouterConfig {
    @Bean
    public RouterFunction<ServerResponse> itemRoute(ItemHandlerFunction itemHandlerFunction) {
        return RouterFunctions
                .route(RequestPredicates.GET(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT).and(RequestPredicates.accept(
                        MediaType.APPLICATION_JSON)),
                       itemHandlerFunction::getAllItems)
                .andRoute(RequestPredicates.GET(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT + "/{id}")
                                           .and(RequestPredicates.accept(
                                                   MediaType.APPLICATION_JSON)),
                          itemHandlerFunction::getOneItem)
                .andRoute(RequestPredicates.POST(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT)
                                           .and(RequestPredicates.accept(
                                                   MediaType.APPLICATION_JSON)),
                          itemHandlerFunction::createItem)
                .andRoute(RequestPredicates.DELETE(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT + "/{id}")
                                           .and(RequestPredicates.accept(
                                                   MediaType.APPLICATION_JSON)),
                          itemHandlerFunction::deleteItem)
                .andRoute(RequestPredicates.PUT(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT + "/{id}")
                                           .and(RequestPredicates.accept(
                                                   MediaType.APPLICATION_JSON)),
                          itemHandlerFunction::updateItem);
    }
}
