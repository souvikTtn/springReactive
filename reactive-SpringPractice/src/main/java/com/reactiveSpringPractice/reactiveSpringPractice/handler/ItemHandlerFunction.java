package com.reactiveSpringPractice.reactiveSpringPractice.handler;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import com.reactiveSpringPractice.reactiveSpringPractice.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ItemHandlerFunction {

    private static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    @Autowired
    private ItemService itemService;

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest) {
        return ServerResponse.ok()
                             .body(itemService.getAllItems(), Item.class);
    }

    public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {
        log.info("=>request received to get one item");
        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = itemService.findById(id);

        //bodyInserters insert an item to serverResponse
        return itemMono.flatMap(item ->
                                        ServerResponse.ok()
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .body(BodyInserters.fromObject(item)))
                       .switchIfEmpty(notFound);
    }


    public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
        log.info("=>request received to create Item");
        Mono<Item> item = serverRequest.bodyToMono(Item.class);
        return item.flatMap(item1 -> ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(itemService.saveItem(item1), Item.class));
    }

    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
        log.info("=>request received to delete Item");
        String id = serverRequest.pathVariable("id");
        Mono<Void> itemMono = itemService.deleteById(id);
        return ServerResponse.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(itemMono, Void.class);
    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
        log.info("=>request received to update Item");
        String id = serverRequest.pathVariable("id");
        /*Mono<Item> updatedItem = serverRequest.bodyToMono(Item.class)
                                              .flatMap(item -> itemService.findById(id).map(currentItem -> {
                                                  currentItem.setPrice(item.getPrice());
                                                  currentItem.setDescription(item.getDescription());
                                                  return currentItem;
                                              }).flatMap(currentItem -> itemService.saveItem(currentItem)));
*/
        Mono<Item> updatedItem = serverRequest.bodyToMono(Item.class)
                                              .flatMap(item -> itemService.findById(id).flatMap(currentItem -> {
                                                  currentItem.setPrice(item.getPrice());
                                                  currentItem.setDescription(item.getDescription());
                                                  return itemService.saveItem(currentItem);
                                              }));

        return updatedItem.flatMap(item ->
                                           ServerResponse.ok()
                                                         .contentType(MediaType.APPLICATION_JSON)
                                                         .body(BodyInserters.fromObject(item)))
                          .switchIfEmpty(notFound);
    }
}
