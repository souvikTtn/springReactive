package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VitualTimeSchedulerTest {
    @Test
    public void test1() {
        Flux<Integer> integerFlux = Flux.interval(Duration.ofSeconds(1))
                                        .map(Long::intValue)
                                        .take(3);
        StepVerifier.create(integerFlux.log())
                    .expectSubscription()
                    .expectNext(0, 1, 2)
                    .verifyComplete();

    }

    @Test
    public void test2() {
        VirtualTimeScheduler.getOrSet();
        Flux<Integer> integerFlux = Flux.interval(Duration.ofSeconds(1))
                                        .map(Long::intValue)
                                        .take(3);
        StepVerifier.withVirtualTime(integerFlux::log)
                    .expectSubscription()
                    .thenAwait(Duration.ofSeconds(3))
                    .expectNext(0, 1, 2)
                    .verifyComplete();

    }
}
