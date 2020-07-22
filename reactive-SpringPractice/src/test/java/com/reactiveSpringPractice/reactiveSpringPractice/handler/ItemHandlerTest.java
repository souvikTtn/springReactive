package com.reactiveSpringPractice.reactiveSpringPractice.handler;

import com.reactiveSpringPractice.reactiveSpringPractice.constant.ItemConstant;
import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import com.reactiveSpringPractice.reactiveSpringPractice.repository.ItemRepository;
import org.junit.Assert;
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
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemHandlerTest {

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
        webTestClient.get().uri(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT)
                     .exchange()
                     .expectBodyList(Item.class)
                     .hasSize(5);
    }


    @Test
    public void getAllItemsTest_approach1() {
        webTestClient.get().uri(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBodyList(Item.class)
                     .consumeWith(response -> {
                         List<Item> items = response.getResponseBody();
                         items.forEach(item -> Assert.assertNotNull(!StringUtils.isEmpty(item.getId())));
                     });
    }


    @Test
    public void getAllItemsTest_approach2() {
        Flux<Item> itemFlux = webTestClient.get().uri(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT)
                                           .exchange()
                                           .expectStatus().isOk()
                                           .returnResult(Item.class).getResponseBody();

        StepVerifier.create(itemFlux)
                    .expectSubscription()
                    .expectNextCount(5)
                    .verifyComplete();
    }

    @Test
    public void createItem() {
        Item item = new Item(null, "Iphone X", 2000);
        webTestClient.post().uri(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT)
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(Mono.just(item), Item.class)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.description").isEqualTo("Iphone X")
                     .jsonPath("$.id").isNotEmpty()
                     .jsonPath("$.price").isEqualTo(2000);
    }

    @Test
    public void deleteItem() {
        webTestClient.delete().uri(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT.concat("/{id}"),"ABC")
                     .exchange()
                     .expectStatus().isOk();


        Flux<Item> itemFlux = webTestClient.get().uri(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT)
                                           .exchange()
                                           .expectStatus().isOk()
                                           .returnResult(Item.class).getResponseBody();

        StepVerifier.create(itemFlux)
                    .expectSubscription()
                    .expectNextCount(4)
                    .verifyComplete();
    }

    @Test
    public void updateItem() {
        Item item = new Item(null, "Bose Headphone", 115);
        webTestClient.put().uri(ItemConstant.ITEM_FUNCTIONAL_ENDPOINT.concat("/{id}"),"ABC")
                     .contentType(MediaType.APPLICATION_JSON)
                     .body(Mono.just(item), Item.class)
                     .exchange()
                     .expectStatus().isOk()
                     .expectBody()
                     .jsonPath("$.description").isEqualTo("Bose Headphone")
                     .jsonPath("$.id").isNotEmpty()
                     .jsonPath("$.price").isEqualTo(115);
    }
}
