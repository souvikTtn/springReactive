package com.reactiveSpringPractice.reactiveSpringPractice.service;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import reactor.core.publisher.Flux;

public interface ItemService {
    Flux<Item> getAllItems();
}
