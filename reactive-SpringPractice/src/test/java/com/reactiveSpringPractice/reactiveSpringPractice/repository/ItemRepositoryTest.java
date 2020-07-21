package com.reactiveSpringPractice.reactiveSpringPractice.repository;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext
@ActiveProfiles("test")
//datamongotest annotation do not load the whole spring context
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

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
    public void findAllItemTest() {
        //comment application.properties for proper execution of test cases
        Flux<Item> itemFlux = itemRepository.findAll();

        StepVerifier.create(itemFlux.log())
                    .expectSubscription()
                    .expectNextCount(5)
                    .verifyComplete();
    }

    @Test
    public void findItemByIdTest() {
        //comment application.properties for proper execution of test cases
        Mono<Item> itemMono = itemRepository.findById("ABC");

        StepVerifier.create(itemMono.log())
                    .expectSubscription()
                    .expectNextMatches(item -> item.getDescription().equals("Bose Headphones"))
                    .verifyComplete();
    }

    @Test
    public void findItemByDescriptionTest() {
        //comment application.properties for proper execution of test cases
        Flux<Item> itemFlux = itemRepository.findByDescription("Bose Headphones");

        StepVerifier.create(itemFlux.log())
                    .expectSubscription()
                    .expectNextCount(1)
                    .verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "Google mini", 100);
        Mono<Item> itemMono = itemRepository.save(item);
        StepVerifier.create(itemMono.log())
                    .expectSubscription()
                    .expectNextMatches(item1 -> item.getId() != null && item1.getDescription().equals("Google mini"))
                    .verifyComplete();

    }

    @Test
    public void updateItem() {
        Mono<Item> itemMono = itemRepository.findById("ABC")
                                            .map(item -> {
                                                item.setPrice(200);
                                                return item;
                                            }).flatMap(item -> itemRepository.save(item));
        StepVerifier.create(itemMono.log())
                    .expectSubscription()
                    .expectNextMatches(item1 -> item1.getPrice() == 200)
                    .verifyComplete();

    }


    @Test
    public void deleteItem() {
        Mono<Void> itemMono = itemRepository.findById("ABC")
                                            .map(Item::getId)
                                            .flatMap(itemRepository::deleteById);
        StepVerifier.create(itemMono.log())
                    .expectSubscription()
                    .verifyComplete();

        StepVerifier.create(itemRepository.findAll())
                    .expectSubscription()
                    .expectNextCount(4)
                    .verifyComplete();

    }
}
