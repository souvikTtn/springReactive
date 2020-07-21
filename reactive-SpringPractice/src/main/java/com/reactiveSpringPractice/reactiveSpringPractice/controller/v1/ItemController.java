package com.reactiveSpringPractice.reactiveSpringPractice.controller.v1;

import com.reactiveSpringPractice.reactiveSpringPractice.constant.ItemConstant;
import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import com.reactiveSpringPractice.reactiveSpringPractice.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}
