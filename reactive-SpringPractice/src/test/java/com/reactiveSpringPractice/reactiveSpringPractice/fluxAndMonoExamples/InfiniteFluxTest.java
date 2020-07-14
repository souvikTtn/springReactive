package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class InfiniteFluxTest {

    @Test
    public void infiniteFluxTest()
    throws InterruptedException {
        //emit data every second
        Flux<Long> longFLux = Flux.interval(Duration.ofSeconds(1)).log();
        longFLux.subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void infiniteFluxTest2() {
        //emit data every second
        Flux<Long> longFLux = Flux.interval(Duration.ofMillis(100)).take(3);
        StepVerifier.create(longFLux.log())
                    .expectSubscription()
                    .expectNext(0L, 1L, 2L)
                    .verifyComplete();


    }
}
