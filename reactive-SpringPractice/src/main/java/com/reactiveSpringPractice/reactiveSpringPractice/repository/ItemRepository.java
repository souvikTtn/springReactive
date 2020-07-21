package com.reactiveSpringPractice.reactiveSpringPractice.repository;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {
    Flux<Item> findByDescription(String description);
}
