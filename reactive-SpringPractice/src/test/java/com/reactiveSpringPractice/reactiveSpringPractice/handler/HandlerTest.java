package com.reactiveSpringPractice.reactiveSpringPractice.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class HandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testFluxControllerApproach1() {
        Flux<Integer> integerFlux = webTestClient.get().uri("/function/flux")
                                                 .exchange()
                                                 .expectStatus().isOk()
                                                 .returnResult(Integer.class)
                                                 .getResponseBody();
        StepVerifier.create(integerFlux)
                    .expectSubscription()
                    .expectNext(1, 2, 3, 4)
                    .verifyComplete();
    }
}
