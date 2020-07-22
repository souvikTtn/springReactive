package com.reactiveSpring.itemClient.controller;

import com.reactiveSpring.itemClient.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemClientController {
    @Autowired
    private WebClient webClient;

    @GetMapping(value = "/client/retrieve")
    public Flux<Item> getAllItemsUsingRetrieve() {
        return webClient.get().uri("/v1/items")
                        .retrieve()
                        .bodyToFlux(Item.class);

    }

    @GetMapping(value = "/client/retrieve/{id}")
    public Mono<Item> getAllItemsUsingRetrieve(@PathVariable("id") String id) {
        return webClient.get().uri("/v1/items/{id}", id)
                        .retrieve()
                        .bodyToMono(Item.class);

    }

    @GetMapping(value = "/client/exchange")
    public Flux<Item> getAllItemsUsingExchange() {
        return webClient.get().uri("/v1/items")
                        .exchange()
                        .flatMapMany(clientResponse ->
                                             clientResponse.bodyToFlux(Item.class));


    }

    @GetMapping(value = "/client/exchange/{id}")
    public Mono<Item> getAllItemsUsingExchange(@PathVariable("id") String id) {
        return webClient.get().uri("/v1/items/{id}", id)
                        .exchange()
                        .flatMap(clientResponse ->
                                         clientResponse.bodyToMono(Item.class));


    }

    @PostMapping("/client/createItem")
    public Mono<Item> createItem(@RequestBody Item item) {
        return webClient.post().uri("/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(item))
                        .retrieve()
                        .bodyToMono(Item.class);

        /*return webClient.post().uri("/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(item))
                        .retrieve()
                        .bodyToMono(Item.class);*/
    }

    @PutMapping("/client/createItem/{id}")
    public Mono<Item> updateItem(@PathVariable("id") String id, @RequestBody Item item) {
        return webClient.put().uri("/v1/items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromObject(item))
                        .retrieve()
                        .bodyToMono(Item.class);
    }

    @DeleteMapping("/client/createItem/{id}")
    public Mono<Void> updateItem(@PathVariable("id") String id) {
        return webClient.delete().uri("/v1/items/{id}", id)
                        .retrieve()
                        .bodyToMono(Void.class);
    }
}
