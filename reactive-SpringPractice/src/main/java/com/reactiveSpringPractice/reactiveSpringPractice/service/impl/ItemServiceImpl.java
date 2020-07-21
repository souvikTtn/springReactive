package com.reactiveSpringPractice.reactiveSpringPractice.service.impl;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import com.reactiveSpringPractice.reactiveSpringPractice.repository.ItemRepository;
import com.reactiveSpringPractice.reactiveSpringPractice.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }
}
