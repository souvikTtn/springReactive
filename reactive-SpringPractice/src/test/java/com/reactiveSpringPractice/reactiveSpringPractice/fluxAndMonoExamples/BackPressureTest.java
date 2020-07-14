package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class BackPressureTest {

    @Test
    public void test1() {
        Flux<Integer> finiteFlux = Flux.range(1, 10);
        StepVerifier.create(finiteFlux.log())
                    .expectSubscription()
                    .thenRequest(1)
                    .expectNext(1)
                    .thenRequest(1)
                    .expectNext(2)
                    .thenCancel()
                    .verify();
    }

    @Test
    public void test2() {
        Flux<Integer> finiteFlux = Flux.range(1, 10);
        finiteFlux.subscribe((element) -> System.out.println("element is " + element),
                             (e) -> System.err.println("exception is " + e),
                             () -> System.out.println("Data flow completed"),
                             (subscription -> subscription.request(4)));

        StepVerifier.create(finiteFlux.log())
                    .expectSubscription()
                    .thenRequest(4)
                    .expectNext(1, 2, 3, 4)
                    .thenCancel()
                    .verify();
    }


    @Test
    public void test3() {
        Flux<Integer> finiteFlux = Flux.range(1, 10);
        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnCancel() {
                System.out.println("on cancel called");
            }

            @Override
            protected void hookOnNext(Integer value) {
                System.out.println("element is " + value);
                request(4);
                if (value == 4) {
                    cancel();
                }
            }
        });
        StepVerifier.create(finiteFlux.log())
                    .expectSubscription()
                    .thenRequest(4)
                    .expectNext(1, 2, 3, 4)
                    .thenCancel()
                    .verify();
    }
}
