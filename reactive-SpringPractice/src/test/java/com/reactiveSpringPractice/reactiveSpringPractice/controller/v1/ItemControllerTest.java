package com.reactiveSpringPractice.reactiveSpringPractice.controller.v1;

import com.reactiveSpringPractice.reactiveSpringPractice.constant.ItemConstant;
import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import com.reactiveSpringPractice.reactiveSpringPractice.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private WebTestClient webTestClient;

    private List<Item> items = Arrays.asList(new Item(null, "LG tv", 2000),
                                             new Item(null, "Apple watch", 3000),
                                             new Item(null, "Samsung tv", 2500),
                                             new Item(null, "Amzon Alexa", 1500),
                                             new Item("ABC", "Bose Headphones", 100));

    @Before
    public void bootstrapItems() {
        itemRepository.deleteAll()
                      .thenMany(Flux.fromIterable(items))
                      .flatMap(item -> itemRepository.save(item))
                      .doOnNext(item -> System.out.println("item saved is ::" + item))
                      .blockLast();

    }

    @Test
    public void getAllItemsTest() {
        webTestClient.get().uri(ItemConstant.ITEM_ENDPOINT)
                     .exchange()
                     .expectBodyList(Item.class)
                     .hasSize(5);
    }

}
