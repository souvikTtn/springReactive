package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFactoryTest {
    List<String> names = Arrays.asList("adam", "eva", "jonas", "martha");

    @Test
    public void fluxFactoryTest1() {
        Flux<String> stringFlux = Flux.fromIterable(names);
        StepVerifier.create(stringFlux.log())
                    .expectNext("adam", "eva", "jonas", "martha")
                    .verifyComplete();
    }

    @Test
    public void fluxFactoryTest2() {
        String[] nameArray = {"adam", "eva", "jonas", "martha"};
        Flux<String> stringFlux = Flux.fromArray(nameArray);
        StepVerifier.create(stringFlux.log())
                    .expectNext("adam", "eva", "jonas", "martha")
                    .verifyComplete();
    }

    @Test
    public void fluxFactoryTest3() {
        Flux<String> stringFlux = Flux.fromStream(names.stream());
        StepVerifier.create(stringFlux.log())
                    .expectNext("adam", "eva", "jonas", "martha")
                    .verifyComplete();
    }

    @Test
    public void monoFactoryTest() {
        Mono<String> stringMono = Mono.fromSupplier(() -> "khanwald");
        StepVerifier.create(stringMono.log())
                    .expectNext("khanwald")
                    .verifyComplete();
    }

    @Test
    public void fluxFactoryTest4() {
        Flux<Integer> integerFlux = Flux.range(1, 5);
        StepVerifier.create(integerFlux.log())
                    .expectNext(1, 2, 3, 4, 5)
                    .verifyComplete();
    }

}
