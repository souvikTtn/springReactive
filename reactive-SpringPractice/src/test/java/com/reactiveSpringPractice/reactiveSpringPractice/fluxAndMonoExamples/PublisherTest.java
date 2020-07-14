package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class PublisherTest {
    List<String> names = Arrays.asList("jonas", "martha", "adam", "eva", "noah", "claudia");

    @Test
    public void coldPublisher()
    throws InterruptedException {
        //all the data is emitted from the beginning of the flux
        Flux<String> stringFlux = Flux.fromIterable(names).delayElements(Duration.ofSeconds(1));
        stringFlux.subscribe(el -> System.out.println("subscriber1 " + el));
        Thread.sleep(2000);
        stringFlux.subscribe(el -> System.out.println("subscriber2 " + el));
        Thread.sleep(4000);
    }

    @Test
    public void hotPublisher()
    throws InterruptedException {
        //all the data is not emitted from the beginning of the flux
        //but from the time of subscription
        Flux<String> stringFlux = Flux.fromIterable(names).delayElements(Duration.ofSeconds(1));
        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();
        connectableFlux.subscribe(el -> System.out.println("subscriber1 " + el));
        Thread.sleep(2000);
        connectableFlux.subscribe(el -> System.out.println("subscriber2 " + el));
        Thread.sleep(4000);
    }


}
