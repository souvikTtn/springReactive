package com.reactiveSpringPractice.reactiveSpringPractice.service.impl;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import com.reactiveSpringPractice.reactiveSpringPractice.repository.ItemRepository;
import com.reactiveSpringPractice.reactiveSpringPractice.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Mono<Item> findById(String id) {
        return itemRepository.findById(id);
    }

    @Override
    public Mono<Item> saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return itemRepository.deleteById(id);
    }

    @Override
    public Mono<Item> updateById(String id, Item item) {
        return itemRepository.findById(id)
                             .flatMap(it -> {
                                 if (!StringUtils.isEmpty(item.getDescription())) {
                                     it.setDescription(item.getDescription());
                                 }
                                 if (Objects.nonNull(item.getPrice())) {
                                     it.setPrice(item.getPrice());
                                 }
                                 return itemRepository.save(it);
                             });
    }
}
