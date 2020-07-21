package com.reactiveSpringPractice.reactiveSpringPractice.controller.v1;

import com.reactiveSpringPractice.reactiveSpringPractice.constant.ItemConstant;
import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import com.reactiveSpringPractice.reactiveSpringPractice.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping(value = ItemConstant.ITEM_ENDPOINT + "/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Item> getAllItemsStream() {
        log.info("-> request received for fetching all the items");
        return itemService.getAllItems();
    }

    @GetMapping(value = ItemConstant.ITEM_ENDPOINT)
    public Flux<Item> getAllItems() {
        log.info("-> request received for fetching all the items");
        return itemService.getAllItems();
    }

    @GetMapping(ItemConstant.ITEM_ENDPOINT + "/{id}")
    public Mono<ResponseEntity<Item>> getItemById(@PathVariable("id") String id) {
        return itemService.findById(id)
                          .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                          .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ItemConstant.ITEM_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemService.saveItem(item);
    }

    @DeleteMapping(ItemConstant.ITEM_ENDPOINT + "/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return itemService.deleteById(id);
    }

    @PutMapping(ItemConstant.ITEM_ENDPOINT + "/{id}")
    public Mono<ResponseEntity<Item>> updateById(@PathVariable("id") String id, @RequestBody Item item) {
        return itemService.updateById(id, item)
                          .map(it -> new ResponseEntity<>(it, HttpStatus.OK))
                          .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
