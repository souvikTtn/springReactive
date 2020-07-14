package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxExceptionTest {
    private List<String> names = Arrays.asList("jonas", "martha", "bartoz");

    @Test
    public void exceptionTest1() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                                      .concatWith(Flux.error(new RuntimeException("error occured")))
                                      .concatWith(Flux.just("claudia"));
        StepVerifier.create(stringFlux.log())
                    .expectSubscription()
                    .expectNext("jonas", "martha", "bartoz")
                    .expectError(RuntimeException.class)
                    .verify();
    }


    @Test
    public void exceptionTest2() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                                      .concatWith(Flux.error(new RuntimeException("error occurred")))
                                      .concatWith(Flux.just("claudia"))
                                      .onErrorResume(e -> {
                                          System.out.println(e.getMessage());
                                          return Flux.just("adam", "eva");
                                      });
        StepVerifier.create(stringFlux.log())
                    .expectSubscription()
                    .expectNext("jonas", "martha", "bartoz", "adam", "eva")
                    .verifyComplete();
    }

    @Test
    public void exceptionTest3() {
        Flux<String> stringFlux = Flux.fromIterable(names)
                                      .concatWith(Flux.error(new RuntimeException("error occurred")))
                                      .concatWith(Flux.just("claudia"))
                                      .onErrorReturn("default");
        StepVerifier.create(stringFlux.log())
                    .expectSubscription()
                    .expectNext("jonas", "martha", "bartoz", "default")
                    .verifyComplete();
    }

    @Test
    public void exceptionTest4() {
        //we can use retry backoff to specify time for retry
        //the next retry will happen after specified duration
        //after that an illegal state exception is thrown
        Flux<String> stringFlux = Flux.fromIterable(names)
                                      .concatWith(Flux.error(new RuntimeException("error occurred")))
                                      .concatWith(Flux.just("claudia"))
                                      .onErrorMap(e -> {
                                          System.out.println(e.getMessage());
                                          return new CustomException("customised error");
                                      })
                                      .retry(1);
        StepVerifier.create(stringFlux.log())
                    .expectSubscription()
                    .expectNext("jonas", "martha", "bartoz")
                    .expectNext("jonas", "martha", "bartoz")
                    .expectError(CustomException.class)
                    .verify();
    }
}
