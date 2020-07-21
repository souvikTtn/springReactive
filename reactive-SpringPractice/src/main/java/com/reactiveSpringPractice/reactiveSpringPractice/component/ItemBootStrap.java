package com.reactiveSpringPractice.reactiveSpringPractice.component;

import com.reactiveSpringPractice.reactiveSpringPractice.document.Item;
import com.reactiveSpringPractice.reactiveSpringPractice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@Profile("!test")
public class ItemBootStrap implements CommandLineRunner {
    @Autowired
    private ItemRepository itemRepository;


    @Override
    public void run(String... args)
    throws Exception {
        log.info("-> bootstrapping items in the database using commandlinerunner");
        Flux<Item> itemFlux = Flux.fromIterable(getBootstrapItems());
        itemRepository.deleteAll()
                      .thenMany(itemFlux)
                      .flatMap(itemRepository::save)
                      .log("saving items")
                      .thenMany(itemRepository.findAll())
                      .subscribe(item -> {
                          log.info("item saved is" + item);
                      });
    }

    public List<Item> getBootstrapItems() {
        return Arrays.asList(new Item(null, "LG tv", 2000),
                             new Item(null, "Apple watch", 3000),
                             new Item(null, "Samsung tv", 2500),
                             new Item(null, "Amzon Alexa", 1500),
                             new Item("ABC", "Bose Headphones", 100));
    }
}
