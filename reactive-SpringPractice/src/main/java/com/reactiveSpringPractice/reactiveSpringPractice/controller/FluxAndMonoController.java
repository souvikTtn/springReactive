package com.reactiveSpringPractice.reactiveSpringPractice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getFluxStream() {
        return Flux.just(1, 2, 3, 4).delayElements(Duration.ofSeconds(1));
        /*return Flux.interval(Duration.ofSeconds(1));*/
    }

    @GetMapping(value = "/fluxstream2", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Long> getFluxStream2() {
        return Flux.interval(Duration.ofSeconds(1));
    }
}
