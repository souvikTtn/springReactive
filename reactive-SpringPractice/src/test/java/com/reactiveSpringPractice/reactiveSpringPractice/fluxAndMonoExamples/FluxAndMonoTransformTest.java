package com.reactiveSpringPractice.reactiveSpringPractice.fluxAndMonoExamples;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformTest {
    List<String> names = Arrays.asList("adam", "eva", "jonas", "martha");

    @Test
    public void transformTest1() {
        Flux<Integer> integerFlux = Flux.range(1, 10);
        Flux<Integer> transformedFlux = integerFlux.map(it -> it * 2);
        integerFlux.map(it -> it * 2).subscribe(System.out::println);
        StepVerifier.create(transformedFlux.log())
                    .expectNext(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
                    .verifyComplete();
    }

    @Test
    public void transformTest2() {
        Flux<Integer> lengthFlux = Flux.fromIterable(names).map(String::length).repeat(0);
        //repeat metho dcan be used if want to repeat the flux
        /* Flux<Integer> lengthFlux = Flux.fromStream(names.stream()).map(String::length);*/
        lengthFlux.subscribe(System.out::println);
        StepVerifier.create(lengthFlux.log())
                    .expectNext(4, 3, 5, 6)
                    .verifyComplete();
    }

    @Test
    public void transformTest3() {
        //flatmap returns flux for each element
        Flux<String> stringFlux = Flux.fromIterable(names);
        //example of flatmap
        Flux<String> newFlux = stringFlux.flatMap(s -> Flux.fromIterable(transform(s))).log();
        newFlux.subscribe(System.out::println);
        StepVerifier.create(newFlux).expectNextCount(8).verifyComplete();
    }


    @Test
    public void transformTest4() {
        //async example without maintaining order
        //window method creates a window of flux
        Flux<String> stringFlux = Flux.fromIterable(names);
        /*Flux<String> newFlux = stringFlux.flatMap(s -> Flux.fromIterable(transform(s))).log();*/
        //Flux<Flux<String>>
        //use flatMapSequential for maintaining the order
        Flux<String> newFlux = stringFlux.window(2)
                                         .flatMap(s -> s.map(this::transform).subscribeOn(parallel()))
                                         .flatMap(Flux::fromIterable)
                                         .log();

        StepVerifier.create(newFlux).expectNextCount(8).verifyComplete();
    }


    @Test
    public void transformTest5() {
        Flux<String> stringFlux = Flux.fromIterable(names);
        Flux<String> newFlux = stringFlux.flatMap(s -> Flux.just(s.toUpperCase())).log();
    }

    private List<String> transform(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "khanwald");
    }
}
