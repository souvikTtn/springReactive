package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class FluxAndMonoCombineTest {
    List<String> dark1 = Arrays.asList("jonas", "martha", "noah");
    List<String> dark2 = Arrays.asList("adam", "eva", "stranger");

    @Test
    public void combineTest1() {
        //merge method works in parallel and does not wait for one publisher to complete data emission
        //therefore there is no guarantee that the order will be maintained
        Flux<String> combinedFlux = Flux.merge(Flux.fromIterable(dark1), Flux.fromIterable(dark2));
        StepVerifier.create(combinedFlux.log())
                    .expectSubscription()
                    .expectNextCount(6)
                    .verifyComplete();
    }

    @Test
    public void combineTest2() {
        Flux<String> combinedFlux = Flux.merge(Flux.fromIterable(dark1).delayElements(Duration.ofSeconds(1)),
                                               Flux.fromIterable(dark2).delayElements(Duration.ofSeconds(1)));
        StepVerifier.create(combinedFlux.log())
                    .expectSubscription()
                    .expectNextCount(6)
                    .verifyComplete();
    }

    @Test
    public void combineTest3() {
        //for maintaining order we can use concat method
        Flux<String> combinedFlux = Flux.concat(Flux.fromIterable(dark1).delayElements(Duration.ofSeconds(1)),
                                                Flux.fromIterable(dark2).delayElements(Duration.ofSeconds(1)));
        StepVerifier.create(combinedFlux.log())
                    .expectSubscription()
                    .expectNextCount(6)
                    .verifyComplete();
    }

    @Test
    public void combineTest4() {
        List<String> firstNames = Arrays.asList("jonas", "bartoz", "martha", "fransizka");
        List<String> lastNames = Arrays.asList("khanwald", "teiderman", "neilsen", "doppler");
        Flux<String> combinedFlux = Flux.zip(Flux.fromIterable(firstNames),
                                             Flux.fromIterable(lastNames), (t1, t2) -> t1 + " " + t2);

        combinedFlux.subscribe(System.out::println);
        StepVerifier.create(combinedFlux.log())
                    .expectSubscription()
                    .expectNextCount(4)
                    .verifyComplete();
    }

    @Test
    public void combineTest5() {
        Mono<String> stringMono = Mono.just("star");
        Flux<Integer> integerFlux = Flux.range(1, 10);
        Flux.merge(integerFlux, stringMono);
    }

}
