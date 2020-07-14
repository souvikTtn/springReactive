package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxFilterTest {

    @Test
    public void fluxFilterTest1() {
        Flux<Integer> integerFlux = Flux.range(1, 10);
        Flux<Integer> filteredFlux = integerFlux.filter(it -> it % 2 == 0);
        filteredFlux.subscribe(System.out::println);
        StepVerifier.create(filteredFlux.log()).expectNextCount(5).verifyComplete();
    }
}
