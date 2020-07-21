package com.reactiveSpringPractice.reactiveSpringPractice.service;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemService {
    Flux<Item> getAllItems();

    Mono<Item> findById(String id);

    Mono<Item> saveItem(Item item);

    Mono<Void> deleteById(String id);

    Mono<Item> updateById(String id,Item item);
}
