package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxMonoTest {
    @Test
    public void fluxTest() {
        Flux<String> stringFlux =
                Flux.just("Spring", "Spring Boot", "Reactive Stream")
                    .concatWith(Flux.error(new RuntimeException("exception")))
                    .log();
        //on invoking subscribe method we are attaching a subscriber to read value form the flux
        stringFlux
                .subscribe(System.out::println, e -> System.err.println(e.getMessage()));

    }

    @Test
    public void fluxTest2() {
        Flux<String> stringFlux =
                Flux.just("Spring", "Spring Boot", "Reactive Stream")
                    /*.concatWith(Flux.error(new RuntimeException("exception")))*/
                    .concatWith(Flux.just("after Error"))
                    .log();
        //once an error occurs no more data is going to be emmited
        //in this case after error is not received
        stringFlux
                .subscribe(System.out::println, e -> System.err.println(e.getMessage()), () -> {
                    System.out.println("completed");
                });

    }

    @Test
    public void fluxTestCase1() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Stream").log();
        StepVerifier.create(stringFlux).expectNext("Spring")
                    .expectNext("Spring Boot")
                    .expectNext("Reactive Stream")
                    .verifyComplete();
        //add verifyComplete at the end to start the flow of the data
    }

    @Test
    public void fluxTestCase2() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Stream")
                                      .concatWith(Flux.error(new RuntimeException("exception")))
                                      .log();
        StepVerifier.create(stringFlux).expectNext("Spring")
                    .expectNext("Spring Boot")
                    .expectNext("Reactive Stream")
                    /*.expectError(RuntimeException.class)*/
                    .expectErrorMessage("exception")
                    .verify();

        /*StepVerifier.create(stringFlux).expectNext("Spring","Spring Boot","Reactive Stream")
                    .expectErrorMessage("exception")
                    .verify();*/
    }

    @Test
    public void fluxTestCase3() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Stream")
                                      .concatWith(Flux.error(new RuntimeException("exception")))
                                      .log();
        StepVerifier.create(stringFlux).expectNextCount(3)
                    .expectError(RuntimeException.class)
                    .verify();

    }

    @Test
    public void monoTestCase1() {
        Mono<String> stringMono = Mono.just("Spring");
        StepVerifier.create(stringMono.log()).expectNext("Spring")
                    .verifyComplete();

    }
}
