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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Test
    public void getItemById() {
        webTestClient.get().uri(ItemConstant.ITEM_ENDPOINT.concat("/{id}"), "ABC")
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.price", 100);
    }

    @Test
    public void getItemById_notFound() {
        webTestClient.get().uri(ItemConstant.ITEM_ENDPOINT.concat("/{id}"), "DEF")
                     .exchange()
                     .expectStatus().isNotFound();
    }

    @Test
    public void createItem() {
        Item item = new Item(null, "Iphone X", 2000);
        webTestClient.post().uri(ItemConstant.ITEM_ENDPOINT)
                     .contentType(MediaType.APPLICATION_JSON_UTF8)
                     .body(Mono.just(item), Item.class)
                     .exchange()
                     .expectStatus().isCreated()
                     .expectBody()
                     .jsonPath("$.description").isEqualTo("Iphone X")
                     .jsonPath("$.id").isNotEmpty()
                     .jsonPath("$.price").isEqualTo(2000);
    }

}
